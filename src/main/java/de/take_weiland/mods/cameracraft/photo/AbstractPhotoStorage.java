package de.take_weiland.mods.cameracraft.photo;

import de.take_weiland.mods.cameracraft.api.img.ImageFilter;
import de.take_weiland.mods.cameracraft.api.photo.PhotoStorage;
import de.take_weiland.mods.commons.Listenables;

import static com.google.common.base.Preconditions.checkPositionIndex;
import static com.google.common.base.Preconditions.checkState;

public abstract class AbstractPhotoStorage implements PhotoStorage {

	protected final boolean isSealed;
	
	protected AbstractPhotoStorage(boolean isSealed) {
		this.isSealed = isSealed;
	}

	@Override
	public boolean isFull() {
		return size() >= capacity();
	}

	@Override
	public boolean canAccept() {
		return !isFull() && !isSealed();
	}

	@Override
	public int getPosition(Integer photoId) {
		int size = size();
		for (int i = 0; i < size; ++i) {
			if (getImpl(i).intValue() == photoId.intValue()) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public Integer get(int index) {
		checkPositionIndex(index, capacity());
		return index < size() ? getImpl(index) : null;
	}
	
	protected abstract Integer getImpl(int index);

	@Override
	public int store(Integer photoId) {
		checkNotSealed();
		if (isFull()) {
			return -1;
		} else {
			storeImpl(photoId);
			Listenables.onChange(this);
			return size() - 1;
		}
	}
	
	protected abstract void storeImpl(Integer photoId);

	@Override
	public void remove(int index) {
		checkNotSealed();
		removeImpl(index);
		Listenables.onChange(this);
	}
	
	protected abstract void removeImpl(int index);
	
	@Override
	public void clear() {
		boolean willChange = size() != 0;
		clearImpl();
		if (willChange) {
			Listenables.onChange(this);
		}
	}
	
	protected abstract void clearImpl();
	
	protected final void checkNotSealed() {
		checkState(!isSealed, "PhotoStorage is sealed!");
	}

	@Override
	public boolean isSealed() {
		return isSealed;
	}

	@Override
	public ImageFilter getFilter() { // default to no filter
		return null;
	}

	@Override
	public void onChange() { }
}
