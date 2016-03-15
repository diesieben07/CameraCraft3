package de.take_weiland.mods.cameracraft.db;

import com.google.common.base.Ticker;
import com.google.common.collect.MapMaker;
import com.google.common.collect.UnmodifiableIterator;
import de.take_weiland.mods.cameracraft.api.img.ImageFilter;
import de.take_weiland.mods.cameracraft.api.photo.Photo;
import de.take_weiland.mods.cameracraft.api.photo.PhotoData;
import de.take_weiland.mods.cameracraft.api.photo.PhotoDatabase;
import de.take_weiland.mods.cameracraft.img.ImageUtil;
import de.take_weiland.mods.commons.util.Async;
import de.take_weiland.mods.commons.util.JavaUtils;
import de.take_weiland.mods.commons.util.Logging;
import gnu.trove.TCollections;
import gnu.trove.TLongCollection;
import gnu.trove.iterator.TLongIterator;
import gnu.trove.list.TLongList;
import gnu.trove.list.array.TLongArrayList;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.ReportedException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.Logger;
import sun.misc.Unsafe;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * <p>Default PhotoDatabase implementation. Uses a java NIO path as storage root, so it is not restricted to the normal
 * file system but could theoretically also load from a zip file or similar.</p>
 * <p>This implementation is threadsafe and performs it's tasks asynchronously.</p>
 * <p>{@link #requestCleanup()} must be called regularly to ensure data is not cached forever.</p>
 *
 * @author diesieben07
 */
@ParametersAreNonnullByDefault
public final class DatabaseImpl implements PhotoDatabase, Iterable<Photo>, Runnable {

    private static final Logger log = Logging.getLogger("CameraCraft|DB");

    private static final Unsafe U = (Unsafe) JavaUtils.unsafe();

    private static final long   SMALLEST_ID = 0;
    private static final String IDS_DAT     = "_ids.dat"; // must start with _ to not conflict with regular base 36 encoded dat files

    // constants for cache entry eviction
    private static final Ticker ticker     = Ticker.systemTicker();
    private static final long   evictAfter = 120000000000L; // 2 minutes in ns

    // field offsets for unsafe CAS
    private static final long NEXT_ID_OFFSET;
    private static final long SAVING_MARK_OFFSET;
    private static final long IDS_OFFSET;

    static {
        try {
            NEXT_ID_OFFSET = U.objectFieldOffset(DatabaseImpl.class.getDeclaredField("nextId"));
            SAVING_MARK_OFFSET = U.objectFieldOffset(DatabaseImpl.class.getDeclaredField("saveMarker"));
            IDS_OFFSET = U.objectFieldOffset(DatabaseImpl.class.getDeclaredField("ids"));
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    // file root, must be a directory and an absolute path
    private final Path root;

    // caches
    private final ConcurrentMap<Long, CacheEntry<CompletableFuture<BufferedImage>>> images = new MapMaker().concurrencyLevel(2).makeMap();
    private final ConcurrentMap<Long, CacheEntry<CompletableFuture<Photo>>>         datas  = new MapMaker().concurrencyLevel(2).makeMap();

    // loaders for above caches
    private final Function<Long, CacheEntry<CompletableFuture<BufferedImage>>> imageLoader = this::loadImage;
    private final Function<Long, CacheEntry<CompletableFuture<Photo>>>         dataLoader  = this::loadData;

    // nextId and array of IDs, array is copied and CAS'd for new IDs for simplicity
    @SuppressWarnings({"FieldCanBeLocal", "unused"}) // used via CAS
    private volatile long   nextId;
    private volatile long[] ids;

    // last time the caches were checked for expired entries
    private long lastCleanTime = ticker.read();

    public DatabaseImpl(Path root) {
        this.root = root.toAbsolutePath();
        initialize();
    }

    /**
     * <p>Load list of existing IDs from disk, must happen from constructor for thread safety.</p>
     */
    private void initialize() {
        Path idFile = null;
        try {
            Files.createDirectories(root);
            idFile = idFile();

            TLongList idList = new TLongArrayList();

            boolean exists = Files.exists(idFile);
            if (exists && Files.isRegularFile(idFile)) {
                nextId = readIds(idList, idFile);
                ids = idList.toArray();
            } else {
                if (exists) {
                    try (Stream<Path> stream = Files.walk(idFile)) {
                        Iterator<Path> it = stream.iterator();

                        while (it.hasNext()) {
                            Files.delete(it.next());
                        }
                    }
                }

                nextId = SMALLEST_ID;
                ids = ArrayUtils.EMPTY_LONG_ARRAY;
            }
        } catch (Throwable x) {
            Path p = idFile;

            CrashReport cr = new CrashReport("Loading CameraCraft Photo Database", x);
            cr.getCategory().addCrashSection("Directory", root);
            cr.getCategory().addCrashSectionCallable("Directory properties", () -> Files.readAttributes(root, "*"));
            cr.getCategory().addCrashSection("ID file", idFile);
            cr.getCategory().addCrashSectionCallable("ID file properties", () -> p == null ? null : Files.readAttributes(p, "*"));

            throw new ReportedException(cr);
        }
    }

    /**
     * <p>Clean up any expired tasks asynchronously, if needed.</p>
     */
    public void requestCleanup() {
        long now = ticker.read();
        if (now - lastCleanTime >= evictAfter) {
            // clean up asynchronously
            Async.commonExecutor().execute(this);
            lastCleanTime = now;
        }
    }

    // called asynchronously to clean up the caches
    @Override
    public void run() {
        cleanup(images);
        cleanup(datas);
    }

    private <V> void cleanup(ConcurrentMap<Long, CacheEntry<V>> map) {
        long now = ticker.read();
        map.forEach((k, v) -> {
            if (v.isEvicted(now)) {
                // only remove if same key-value pair is still there
                // just ignore if value was replaced
                map.remove(k, v);
            }
        });
    }

    @SuppressWarnings("unused")
    private volatile Boolean saveMarker;

    private boolean casMarker(@Nullable Object old, @Nullable Object _new) {
        return U.compareAndSwapObject(this, SAVING_MARK_OFFSET, old, _new);
    }

    public void save() {
        if (casMarker(null, Boolean.TRUE)) {
            Async.commonExecutor().execute(() -> {
                try {
                    saveIds(ids, idFile());
                } catch (IOException e) {
                    log.error("Failed to write ids file", e);
                } finally {
                    saveMarker = null;
                }
            });
        }
    }

    private static void saveIds(long[] ids, Path path) throws IOException {
        // todo NIO solution?
        try (DataOutputStream out = new DataOutputStream(Files.newOutputStream(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING))) {
            out.writeInt(ids.length);

            for (long id : ids) {
                out.writeLong(id);
            }
        }
    }

    private long readIds(TLongCollection ids, Path file) throws IOException {
        long next = SMALLEST_ID;

        // todo: proper NIO solution? idk.
        try (DataInputStream in = new DataInputStream(Files.newInputStream(file))) {
            int n = in.readInt();
            for (int i = 0; i < n; i++) {
                long id = in.readLong();
                ids.add(id);
                if (id > next) next = id;
            }
        }

        return next;
    }

    @Override
    public BufferedImage getImage(long id) {
        return getImageAsync(id).join();
    }

    @Override
    public CompletableFuture<BufferedImage> getImageAsync(long id) {
        return getFromCache(images, id, imageLoader).value;
    }

    private CacheEntry<CompletableFuture<BufferedImage>> loadImage(long id) {
        Path path = imagePathFor(id);

        CompletableFuture<BufferedImage> future = CompletableFuture.supplyAsync(() -> {
            try {
                return ImageIO.read(path.toUri().toURL());
            } catch (Throwable e) {
                throw new CompletionException(e);
            }
        }, Async.commonExecutor());

        return new CacheEntry<>(future);
    }

    @Override
    public CompletionStage<BufferedImage> setImage(long id, BufferedImage image) {
        Path path = imagePathFor(id);

        return setNewEntry(images, id, () -> {
            writeImageUnchecked(image, path);
            return image;
        });
    }

    @Override
    public CompletionStage<BufferedImage> applyFilter(long id, ImageFilter filter) {
        Path path = imagePathFor(id);

        return applyEntryModification(images, imageLoader, id, (oldImg, oldX) -> {
            BufferedImage newImg = filter.apply(ImageUtil.clone(oldImg));
            writeImageUnchecked(newImg, path);
            return newImg;
        });
    }

    @Override
    public CompletionStage<Long> saveNewImage(BufferedImage image, @Nullable ImageFilter filter) {
        long id = nextId();

        CompletableFuture<BufferedImage> future;
        if (filter == null) {
            future = CompletableFuture.completedFuture(image);
        } else {
            future = CompletableFuture.supplyAsync(() -> filter.apply(image), Async.commonExecutor());
        }

        Path path = imagePathFor(id);

        future = future
                .whenCompleteAsync((img, x) -> {
                    if (img != null) {
                        writeImageUnchecked(img, path);
                    }
                });

        CacheEntry<CompletableFuture<BufferedImage>> entry = new CacheEntry<>(future);
        images.put(id, entry);

        return future.thenApply(img -> id);
    }

    @Override
    public Photo getPhoto(long id) {
        return getPhotoAsync(id).join();
    }

    @Override
    public CompletableFuture<Photo> getPhotoAsync(long id) {
        return getFromCache(datas, id, this.dataLoader).value;
    }

    private CacheEntry<CompletableFuture<Photo>> loadData(long id) {
        Path path = dataPathFor(id);

        CompletableFuture<Photo> future = CompletableFuture.supplyAsync(() -> {
            try (DataInputStream in = new DataInputStream(path.getFileSystem().provider().newInputStream(path))) {
                return new DelegatingPhoto(PhotoData.read(in), id, DatabaseImpl.this);
            } catch (Throwable x) {
                throw new CompletionException(x);
            }
        });

        return new CacheEntry<>(future);
    }

    @Override
    public CompletionStage<Photo> setData(long id, PhotoData data) {
        Path path = dataPathFor(id);

        return setNewEntry(datas, id, () -> {
            writeData(data, path);
            return new DelegatingPhoto(data, id, this);
        });
    }

    private static void writeData(PhotoData data, Path path) {
        try (DataOutputStream out = new DataOutputStream(path.getFileSystem().provider().newOutputStream(path))) {
            data.write(out);
        } catch (IOException x) {
            throw new UncheckedIOException(x);
        }
    }

    @Override
    public long nextId() {
        long next = U.getAndAddLong(this, NEXT_ID_OFFSET, 1);

        long[] idsOld;
        long[] idsNew;
        do {
            idsOld = ids;
            idsNew = Arrays.copyOf(idsOld, idsOld.length + 1);
            idsNew[idsOld.length] = next;
        } while (!U.compareAndSwapObject(this, IDS_OFFSET, idsOld, idsNew));

        return next;
    }

    @Override
    public TLongList ids() {
        return TCollections.unmodifiableList(TLongArrayList.wrap(ids));
    }

    @Override
    public TLongIterator idIterator() {
        return new IdIterator(ids);
    }

    @Override
    public Iterable<Photo> getPhotos() {
        return this;
    }

    @Override
    public Iterator<Photo> iterator() {
        long[] ids = this.ids;

        class It extends UnmodifiableIterator<Photo> {

            private int idx;

            @Override
            public Photo next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return getPhoto(ids[idx++]);
            }

            @Override
            public boolean hasNext() {
                return idx != ids.length;
            }
        }

        return new It();
    }

    /**
     * <p>Load given key from cache with given loader atomically.</p>
     *
     * @param cache  the cache
     * @param key    the key
     * @param loader the loader
     * @return computed value
     */
    private static <K, V> CacheEntry<V> getFromCache(ConcurrentMap<K, CacheEntry<V>> cache, K key, Function<K, CacheEntry<V>> loader) {
        CacheEntry<V> entry = cache.get(key);
        if (entry == null) {
            entry = cache.computeIfAbsent(key, loader);
        }
        return entry.accessed();
    }

    /**
     * <p>Set new value in the cache asynchronously while waiting for any currently loading values on this key.</p>
     *
     * @param cache    the cache
     * @param key      the key
     * @param supplier the value supplier
     * @return new CompletableFuture
     */
    private static <K, V> CompletableFuture<V> setNewEntry(ConcurrentMap<K, CacheEntry<CompletableFuture<V>>> cache, K key, SupplierBiFuncAdapter<V> supplier) {
        CacheEntry<CompletableFuture<V>> _new = new CacheEntry<>(new CompletableFuture<>());
        CacheEntry<CompletableFuture<V>> old = cache.put(key, _new);

        nullableWhenComplete(old == null ? null : old.value, _new.value, supplier);
        return _new.value;
    }

    @FunctionalInterface
    private interface SupplierBiFuncAdapter<V> extends BiFunction<Object, Object, V>, Supplier<V> {

        @Override
        default V apply(Object o, Object o2) {
            return get();
        }

    }

    /**
     * <p>Apply a modification to an existing value.</p>
     *
     * @param cache    the cache
     * @param loader   loader for the cache
     * @param key      key
     * @param function the modification function
     * @return new CompletableFuture
     */
    private static <K, V> CompletableFuture<V> applyEntryModification(ConcurrentMap<K, CacheEntry<CompletableFuture<V>>> cache, Function<K, CacheEntry<CompletableFuture<V>>> loader, K key, BiFunction<V, Throwable, V> function) {
        CacheEntry<CompletableFuture<V>> _new = new CacheEntry<>(new CompletableFuture<>());
        CacheEntry<CompletableFuture<V>> old;
        do {
            old = getFromCache(cache, key, loader);
        } while (!cache.replace(key, old, _new));

        nullableWhenComplete(old.value, _new.value, function);
        return _new.value;
    }

    /**
     * <p>If old is non-null waits for old to complete then completes _new with result of {@code function(old.value, old.exception)}, otherwise completes
     * _new directly with the result of {@code function(null, null)}. Both happen asynchronously.</p>
     *
     * @param old      potential old value to wait for
     * @param _new     new future to complete
     * @param function mapping function
     */
    private static <V> void nullableWhenComplete(@Nullable CompletableFuture<V> old, CompletableFuture<V> _new, BiFunction<? super V, ? super Throwable, V> function) {
        if (old == null) {
            Async.commonExecutor().execute(() -> {
                try {
                    _new.complete(function.apply(null, null));
                } catch (Throwable x) {
                    _new.completeExceptionally(x);
                }
            });
        } else {
            old.whenCompleteAsync((oldV, oldX) -> {
                try {
                    _new.complete(function.apply(oldV, oldX));
                } catch (Throwable x) {
                    _new.completeExceptionally(x);
                }
            }, Async.commonExecutor());
        }
    }

    Path idFile() {
        return root.resolve(IDS_DAT);
    }

    private Path path(long id, String suffix) {
        return root.resolve(Long.toUnsignedString(id, 36) + suffix);
    }

    Path imagePathFor(long id) {
        return path(id, ".png");
    }

    Path dataPathFor(long id) {
        return path(id, ".dat");
    }

    static void writeImageUnchecked(BufferedImage img, Path path) {
        try (OutputStream out = path.getFileSystem().provider().newOutputStream(path)) {
            ImageIO.write(img, "PNG", out);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    static final class CacheEntry<V> {

        final V value;
        long lastAccess;

        CacheEntry(V value) {
            this.value = value;
            accessed();
        }

        CacheEntry<V> accessed() {
            lastAccess = ticker.read();
            return this;
        }

        boolean isEvicted(long now) {
            return now - lastAccess >= evictAfter;
        }
    }

    static final class IdIterator implements TLongIterator {

        private final long[] arr;
        private int idx = 0;

        IdIterator(long[] arr) {
            this.arr = arr;
        }

        @Override
        public long next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return arr[idx++];
        }

        @Override
        public boolean hasNext() {
            return idx != arr.length;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

}
