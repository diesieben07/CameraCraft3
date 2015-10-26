package de.take_weiland.mods.cameracraft.photo;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.primitives.Longs;
import de.take_weiland.mods.cameracraft.api.img.ImageFilter;
import de.take_weiland.mods.cameracraft.api.photo.Photo;
import de.take_weiland.mods.cameracraft.api.photo.PhotoDatabase;
import gnu.trove.TCollections;
import gnu.trove.iterator.TLongIterator;
import gnu.trove.set.TLongSet;
import gnu.trove.set.hash.TLongHashSet;
import net.minecraftforge.common.DimensionManager;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

/**
 * @author diesieben07
 */
public final class DatabaseImpl implements PhotoDatabase, Iterable<Photo> {

    public static final String IDS_DAT = "_ids.dat";
    public static DatabaseImpl current;

    private final File root;

    private final LoadingCache<Long, PhotoImpl> cache = CacheBuilder.newBuilder()
            .concurrencyLevel(2)
            .expireAfterAccess(60, TimeUnit.SECONDS)
            .build(new DataLoader());

    private final TLongSet ids = new TLongHashSet();
    private final TLongSet idsUnmod = TCollections.unmodifiableSet(ids);
    private long nextId;
    private boolean idsDirty = false;

    private DatabaseImpl(File root) {
        this.root = root;
        try {
            loadIds();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static void onServerStart() {
        File root = new File(DimensionManager.getCurrentSaveRootDirectory(), "cameracraft");
        current = new DatabaseImpl(root);
    }

    public BufferedImage loadImage(long id) throws IOException {
        return ImageIO.read(new File(root, fileName(id, ".png")));
    }

    public InputStream openImageStream(long id) {
        try {
            return new BufferedInputStream(new FileInputStream(file(id, "png")));
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Unknown PhotoID", e);
        }
    }

    private File file(long id, String ext) {
        return new File(root, fileName(id) + '.' + ext);
    }

    @Override
    public void saveImage(long id, BufferedImage image, ImageFilter filter) throws IOException {
        if (filter != null) {
            image = filter.apply(image);
        }
        ImageIO.write(image, "PNG", new File(root, fileName(id, ".png")));
    }

    @Override
    public void applyFilter(long id, ImageFilter filter) throws IOException {
        File file = new File(root, fileName(id, ".png"));
        ImageIO.write(filter.apply(ImageIO.read(file)), "PNG", file);
    }

    private static String fileName(long id, String ext) {
        return Long.toString(id, 36) + ext;
    }

    private static String fileName(long id) {
        return Long.toString(id, 36);
    }

    private void loadIds() throws IOException {
        File idFile = new File(root, IDS_DAT);
        if (idFile.exists() && !idFile.isFile()) {
            Files.delete(idFile.toPath());
        }
        com.google.common.io.Files.createParentDirs(idFile);
        if (!idFile.exists()) {
            Files.write(idFile.toPath(), Longs.toByteArray(0), StandardOpenOption.CREATE);
        }
        if (idFile.canRead()) {
            boolean foundFirst = false;
            byte[] buf = new byte[8];
            try (InputStream in = new BufferedInputStream(new FileInputStream(idFile))) {
                do {
                    if (!readFullyOpt(buf, in)) {
                        break;
                    }
                    long l = Longs.fromByteArray(buf);
                    if (!foundFirst) {
                        nextId = l;
                        foundFirst = true;
                    } else {
                        ids.add(l);
                    }
                } while (true);
            }
            if (!foundFirst) {
                throw new IOException("_ids.dat is invalid");
            }
        } else {
            throw new IOException("Could not read _ids.dat");
        }
    }

    private boolean readFullyOpt(byte[] b, InputStream in) throws IOException {
        int len = b.length;
        int n = 0;
        while (n < len) {
            int count = in.read(b, n, len - n);
            if (count < 0)
                return false;
            n += count;
        }
        return true;
    }

    @Override
    public TLongSet getPhotoIDs() {
        return idsUnmod;
    }

    @Override
    public Iterable<Photo> getPhotos() {
        return this;
    }

    @Override
    public Iterator<Photo> iterator() {
        TLongIterator it = ids.iterator();
        return new UnmodifiableIterator<Photo>() {
            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public Photo next() {
                return getPhoto(it.next());
            }
        };
    }

    @Override
    public Photo getPhoto(long id) {
        return cache.getUnchecked(id);
    }

    @Override
    public void store(long photoID, Photo data) {
        File file = fileFor(photoID);
        try (DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file), 128))) {
            PhotoImpl.write(data, out);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public long nextId() {
        long id = nextId++;
        ids.add(id);
        idsDirty = true;
        return id;
    }

    private File fileFor(long id) {
        String name = Long.toUnsignedString(id, 36) + ".dat";
        return new File(root, name);
    }

    public void save() {
        if (idsDirty) {
            File ids = new File(root, IDS_DAT);
            try {
                try (OutputStream out = new BufferedOutputStream(new FileOutputStream(ids))) {
                    writeLong(out, nextId);
                    TLongIterator it = this.ids.iterator();
                    while (it.hasNext()) {
                        writeLong(out, it.next());
                    }
                }
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
            idsDirty = false;
        }
    }

    private static void writeLong(OutputStream out, long l) throws IOException {
        for (int i = 7; i >= 0; i--) {
          out.write((int) (l & 0xffL));
          l >>= 8;
        }
    }

    private final class DataLoader extends CacheLoader<Long, PhotoImpl> {

        @Override
        public PhotoImpl load(@Nonnull Long id) throws Exception {
            if (!ids.contains(id)) {
                throw new NoSuchElementException("Unknown ID " + id);
            }
            File file = fileFor(id);
            if (!file.isFile()) {
                throw new FileNotFoundException("Corrupt Database, ID " + id + " missing");
            }
            if (!file.canRead()) {
                throw new IOException("File " + file + " is not readable");
            }
            try (DataInputStream in = new DataInputStream(new FileInputStream(file))) {
                return PhotoImpl.load(id, in);
            }
        }
    }

}
