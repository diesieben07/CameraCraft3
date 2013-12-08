package de.take_weiland.mods.cameracraft.photo;

import static com.google.common.base.Preconditions.checkPositionIndex;
import static com.google.common.base.Preconditions.checkState;
import de.take_weiland.mods.cameracraft.api.img.ImageFilter;
import de.take_weiland.mods.cameracraft.api.photo.PhotoStorage;
import de.take_weiland.mods.commons.util.ListenerArrayList;
import de.take_weiland.mods.commons.util.ListenerList;

public abstract class AbstractPhotoStorage implements PhotoStorage {

	private final ListenerList<PhotoStorage> listeners = ListenerArrayList.<PhotoStorage>create(this);
	protected final boolean isSealed;
	
	protected AbstractPhotoStorage(boolean isSealed) {
		this.isSealed = isSealed;
	}

	@Override
	public boolean isFull() {
		return size() >= capacity();
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
			onChange();
			return size() - 1;
		}
	}
	
	protected abstract void storeImpl(String photoId);

	@Override
	public void remove(int index) {
		checkNotSealed();
		removeImpl(index);
		onChange();
	}
	
	protected abstract void removeImpl(int index);
	
	@Override
	public void clear() {
		boolean willChange = size() != 0;
		clearImpl();
		if (willChange) {
			onChange();
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
	
	protected final void onChange() {
		listeners.onChange();
	}

	@Override
	public void addListener(Listener<? super PhotoStorage> l) {
		listeners.add(l);
	}

	@Override
	public void removeListener(Listener<? super PhotoStorage> l) {
		listeners.remove(l);
	}

	@Override
	public ImageFilter getFilter() { // default to no filter
		return null;
	}

}
