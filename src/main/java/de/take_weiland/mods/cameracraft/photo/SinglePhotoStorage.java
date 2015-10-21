package de.take_weiland.mods.cameracraft.photo;

/**
 * @author diesieben07
 */
public final class SinglePhotoStorage extends AbstractPhotoStorage {

    private final long photoId;

    public SinglePhotoStorage(long photoId) {
        this.photoId = photoId;
    }

    @Override
    protected long getImpl(int index) {
        return photoId;
    }

    @Override
    public int capacity() {
        return 1;
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public boolean isSealed() {
        return true;
    }

    @Override
    protected void storeImpl(long photoId) {}

    @Override
    protected void removeImpl(int index) {}

    @Override
    protected void clearImpl() {}
}
