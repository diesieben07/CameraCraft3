package de.take_weiland.mods.cameracraft.db;

import de.take_weiland.mods.commons.util.JavaUtils;
import gnu.trove.set.TLongSet;
import sun.misc.Unsafe;

import java.io.File;
import java.lang.reflect.Field;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author diesieben07
 */
public class IdCollection {

    private static final Unsafe U = (Unsafe) JavaUtils.unsafe();

    private static final int INIT = 0;
    private static final int LOADING = 1;

    private static final long NEXT_ID_OFFSET;

    static {
        try {
            Field nextIdField = IdCollection.class.getDeclaredField("nextId");
            NEXT_ID_OFFSET = U.objectFieldOffset(nextIdField);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private final File file;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    private volatile long nextId;

    public IdCollection(File file) {
        this.file = file;
    }

    private void load() {
        try {
            lock.writeLock().lock();

        } finally {
            lock.writeLock().unlock();
        }
    }

    public long nextId() {
        long next = U.getAndAddLong(this, NEXT_ID_OFFSET, 1);
        return next;
    }

    public TLongSet allIds() {
        return null;
    }

}
