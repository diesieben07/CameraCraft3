package de.take_weiland.mods.cameracraft.photo;

import de.take_weiland.mods.cameracraft.api.img.ImageFilter;
import de.take_weiland.mods.cameracraft.api.photo.PhotoStorage;

import static com.google.common.base.Preconditions.checkPositionIndex;
import static com.google.common.base.Preconditions.checkState;

public abstract class AbstractPhotoStorage implements PhotoStorage {

	@Override
	public int indexOf(long photoId) {
		int size = size();
		for (int i = 0; i < size; ++i) {
			if (getImpl(i) == photoId) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public long get(int index) {
		checkPositionIndex(index, size());
		return getImpl(index);
	}
	
	protected abstract long getImpl(int index);

	@Override
	public int store(long photoId) {
		checkNotSealed();
		if (isFull()) {
			return -1;
		} else {
			storeImpl(photoId);
			return size() - 1;
		}
	}
	
	protected abstract void storeImpl(long photoId);

	@Override
	public void remove(int index) {
		checkNotSealed();
		removeImpl(index);
	}
	
	protected abstract void removeImpl(int index);
	
	@Override
	public void clear() {
		clearImpl();
	}
	
	protected abstract void clearImpl();
	
	protected final void checkNotSealed() {
		checkState(!isSealed(), "PhotoStorage is sealed!");
	}

	@Override
	public ImageFilter getFilter() { // default to no filter
		return null;
	}

}
