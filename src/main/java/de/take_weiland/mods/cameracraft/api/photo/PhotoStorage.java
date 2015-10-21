package de.take_weiland.mods.cameracraft.api.photo;

import de.take_weiland.mods.cameracraft.api.img.ImageFilter;
import gnu.trove.iterator.TLongIterator;

/**
 * <p>A storage for photos.</p>
 *
 * @author diesieben07
 *
 */
public interface PhotoStorage extends Iterable<Long> {

    /**
     * <p>An unboxed iterator, should be used over {@link #iterator()}.</p>
     * @return a new {@code TLongIterator}
     */
    TLongIterator longIterator();

    /**
     * <p>Get the total capacity of this storage.</p>
	 * @return the capacity of this storage
	 */
	int capacity();
	
	/**
     * <p>Get the current number of photos stored in this storage.</p>
	 * @return the current number of stored photos
	 */
	int size();

    /**
     * <p>Whether this storage is full and cannot store any more photos. This is equivalent to checking
     * {@code size() == capacity()}.</p>
     * @return whether this storage is full
     */
    default boolean isFull() {
        return size() == capacity();
    }

    /**
     * <p>Whether this storage can store another photo.</p>
     *
     * @return true if this storage is neither sealed nor full
     */
    default boolean canStore() {
        return !isFull() && !isSealed();
    }

    /**
     * <p>Whether this storage is sealed and cannot store new photos.</p>
	 * @return whether this storage is sealed
	 */
	boolean isSealed();
	
	/**
     * <p>Get any image filter to be applied to every photo getting stored into this storage.</p>
	 * @return a filter to apply or null for no filter
	 */
	ImageFilter getFilter();
	
	// Query operations
	
	/**
     * <p>Get the photo ID stored at the given index.</p>
	 * @param index the index
	 * @return the photo ID
	 */
	long get(int index);
	
	/**
     * <p>Get the index of the given photo ID.</p>
	 * @param photoId the photo ID
	 * @return the in index of the photo ID or -1 if the given ID is not stored in this storage
	 */
	int indexOf(long photoId);

	/**
     * <p>Add the given photo ID to the end of this storage..</p>
	 * @param photoId the photo ID
	 * @return the index of the photo ID or -1 if the storage is full or sealed
	 */
	int store(long photoId);
	
	/**
     * <p>Remove the photo at the given index, shifting any subsequent IDs to the front (subtracting one from their
     * indices).</p>
	 * @param index the index
	 */
	void remove(int index);
	
	/**
     * <p>Remove all photos from this storage.</p>
	 */
	void clear();
	
}
