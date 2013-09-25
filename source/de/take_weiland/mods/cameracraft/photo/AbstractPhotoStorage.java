package de.take_weiland.mods.cameracraft.photo;

import static com.google.common.base.Preconditions.checkPositionIndex;
import static com.google.common.base.Preconditions.checkState;
import de.take_weiland.mods.cameracraft.api.camera.PhotoStorage;

public abstract class AbstractPhotoStorage implements PhotoStorage {

	protected final boolean isSealed;
	
	protected AbstractPhotoStorage(boolean isSealed) {
		this.isSealed = isSealed;
	}

	@Override
	public boolean isFull() {
		return size() < capacity();
	}

	@Override
	public int getPosition(String photoId) {
		int size = size();
		for (int i = 0; i < size; ++i) {
			if (getImpl(i).equals(photoId)) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public String get(int index) {
		checkPositionIndex(index, capacity());
		return index < size() ? getImpl(index) : null;
	}
	
	protected abstract String getImpl(int index);

	@Override
	public int store(String photoId) {
		checkNotSealed();
		if (isFull()) {
			return -1;
		} else {
			storeImpl(photoId);
			return size() - 2;
		}
	}

	protected final void checkNotSealed() {
		checkState(!isSealed, "PhotoStorage is sealed!");
	}
	
	protected abstract void storeImpl(String photoId);

	@Override
	public boolean isSealed() {
		return isSealed;
	}

}
