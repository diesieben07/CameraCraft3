package de.take_weiland.mods.cameracraft.photo;

import de.take_weiland.mods.cameracraft.api.img.ImageFilter;
import de.take_weiland.mods.cameracraft.api.photo.PhotoStorage;
import gnu.trove.iterator.TLongIterator;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

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
	public final void remove(int index) {
		checkNotSealed();
		if (size() == 1) {
            clearImpl();
        } else {
            removeImpl(index);
        }
	}
	
	protected abstract void removeImpl(int index);
	
	@Override
	public final void clear() {
		checkNotSealed();
        clearImpl();
	}

	@Override
	public long[] toLongArray() {
		long[] result = new long[size()];
        for (int i = 0, len = size(); i < len; i++) {
            result[i] = getImpl(i);
        }
        return result;
	}

	protected abstract void clearImpl();
	
	protected final void checkNotSealed() {
		checkState(!isSealed(), "PhotoStorage is sealed!");
	}

	@Override
	public ImageFilter getFilter() { // default to no filter
		return null;
	}

    @Override
    public Iterator<Long> iterator() {
        return new BoxedIterator(this);
    }

    @Override
    public TLongIterator longIterator() {
        return new UnboxedIterator(this);
    }

    @Override
    public void forEach(Consumer<? super Long> action) {
        int len = size();
        for (int i = 0; i < len; i++) {
            action.accept(get(i));
        }
    }

    private static abstract class IteratorImpl {

		final AbstractPhotoStorage storage;
		int idx;

		IteratorImpl(AbstractPhotoStorage storage) {
			this.storage = storage;
		}

		public boolean hasNext() {
			return idx < storage.size();
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	private static final class BoxedIterator extends IteratorImpl implements Iterator<Long> {

		BoxedIterator(AbstractPhotoStorage storage) {
			super(storage);
		}

		@Override
		public Long next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			return storage.getImpl(idx++);
		}
	}

	private static final class UnboxedIterator extends IteratorImpl implements TLongIterator {

		UnboxedIterator(AbstractPhotoStorage storage) {
			super(storage);
		}

		@Override
		public long next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			return storage.getImpl(idx++);
		}

	}

}
