package de.take_weiland.mods.cameracraft.photo;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.primitives.Longs;
import de.take_weiland.mods.cameracraft.api.photo.PhotoData;
import de.take_weiland.mods.cameracraft.api.photo.PhotoDatabase;
import gnu.trove.TCollections;
import gnu.trove.iterator.TLongIterator;
import gnu.trove.set.TLongSet;
import gnu.trove.set.hash.TLongHashSet;

import javax.annotation.Nonnull;
import java.io.*;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

/**
 * @author diesieben07
 */
public final class DatabaseImpl implements PhotoDatabase, Iterable<PhotoData> {

    public static final String IDS_DAT = "_ids.dat";
    public static DatabaseImpl current;

    private final File root;

    private final LoadingCache<Long, PhotoDataImpl> cache = CacheBuilder.newBuilder()
            .concurrencyLevel(2)
            .expireAfterAccess(60, TimeUnit.SECONDS)
            .build(new DataLoader());

    private final TLongSet ids = new TLongHashSet();
    private final TLongSet idsUnmod = TCollections.unmodifiableSet(ids);
    private long nextId;
    private boolean idsDirty = false;

    public DatabaseImpl(File root) {
        this.root = root;
        try {
            loadIds();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void loadIds() throws IOException {
        File idFile = new File(root, IDS_DAT);
        if (!idFile.isFile()) {
            Files.delete(idFile.toPath());
        }
        if (idFile.canRead()) {
            boolean foundFirst = false;
            byte[] buf = new byte[8];
            try (InputStream in = new BufferedInputStream(new FileInputStream(idFile))) {
                int i;
                do {
                    i = readFullyOpt(buf, in);
                    if (i < 0) {
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

    private int readFullyOpt(byte[] b, InputStream in) throws IOException {
        int len = b.length;
        int n = 0;
        while (n < len) {
            int count = in.read(b, n, len - n);
            if (count < 0)
                return -1;
            n += count;
        }
        return n;
    }

    @Override
    public TLongSet getPhotoIDs() {
        return idsUnmod;
    }

    @Override
    public Iterable<PhotoData> getPhotos() {
        return this;
    }

    @Override
    public Iterator<PhotoData> iterator() {
        TLongIterator it = ids.iterator();
        return new UnmodifiableIterator<PhotoData>() {
            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public PhotoData next() {
                return getPhoto(it.next());
            }
        };
    }

    @Override
    public PhotoData getPhoto(long id) {
        return cache.getUnchecked(id);
    }

    @Override
    public void store(long photoID, PhotoData data) {
        File file = fileFor(photoID);
        try (DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file), 128))) {
            PhotoDataImpl.write(data, out);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public long nextId() {
        long id = nextId++;
        idsDirty = true;
        return id;
    }

    private File fileFor(long id) {
        String name = Long.toUnsignedString(id, 36) + ".dat";
        return new File(root, name);
    }

    public void save() {
        if (idsDirty) {
            File ids = new File(IDS_DAT);
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

    private final class DataLoader extends CacheLoader<Long, PhotoDataImpl> {

        @Override
        public PhotoDataImpl load(@Nonnull Long id) throws Exception {
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
                return PhotoDataImpl.load(id, in);
            }
        }
    }

}
