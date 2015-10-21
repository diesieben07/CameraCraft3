package de.take_weiland.mods.cameracraft.api.photo;

import de.take_weiland.mods.cameracraft.api.img.ImageFilter;
import gnu.trove.iterator.TLongIterator;

/**
 * Represents something that can store photos
 * @author diesieben07
 *
 */
public interface PhotoStorage extends Iterable<Long> {

    TLongIterator longIterator();

    /**
	 * @return the amount of photos this PhotoStorage can store
	 */
	int capacity();
	
	/**
	 * @return the number of photos this PhotoStorage currently has stored
	 */
	int size();

    /**
     * @return true if <code>{@link #size()} == {@link #capacity()}</code>
     */
    default boolean isFull() {
        return size() == capacity();
    }

    /**
     * <p>Check if this PhotoStorage can store another photo.</p>
     *
     * @return true if this storage is neither sealed nor full
     */
    default boolean canStore() {
        return !isFull() && !isSealed();
    }

    /**
	 * @return whether this PhotoStorage is sealed (cannot store new photos)
	 */
	boolean isSealed();
	
	/**
	 * Get an image filter to be applied to every photo getting stored into this storage
	 * @return the ImageFilter to apply
	 */
	ImageFilter getFilter();
	
	// Query operations
	
	/**
	 * gets the photoId at the given positionIndex
	 * @param position
	 * @return
	 */
	long get(int position);
	
	/**
	 * search for the given photoId
	 * @param photoId the photoId to search for
	 * @return the in index of the photoId (or -1 if not found)
	 */
	int indexOf(long photoId);

	/**
	 * sets the first free slot to the given photoId
	 * @param photoId
	 * @return the position the photoId was added or -1 if no free positions are available
	 */
	int store(long photoId);
	
	/**
	 * remove the photo at the given position
	 * @param position
	 */
	void remove(int position);
	
	/**
	 * remove all photos from this storage
	 */
	void clear();
	
}
