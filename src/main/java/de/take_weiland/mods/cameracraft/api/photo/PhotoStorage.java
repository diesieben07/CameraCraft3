package de.take_weiland.mods.cameracraft.api.photo;

import java.util.List;

import de.take_weiland.mods.cameracraft.api.img.ImageFilter;
import de.take_weiland.mods.commons.util.Listenable;

/**
 * Represents something that can store photos
 * @author Take Weiland
 *
 */
public interface PhotoStorage extends Listenable<PhotoStorage> {

	List<String> getPhotos();
	
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
	boolean isFull();
	
	/**
	 * @return true if this PhotoStorage can store another photo
	 */
	boolean canAccept();
	
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
	String get(int position);
	
	/**
	 * search for the given photoId
	 * @param photoId the photoId to search for
	 * @return the in index of the photoId (or -1 if not found)
	 */
	int getPosition(String photoId);
	
	/**
	 * sets the first free slot to the given photoId 
	 * @param photoId
	 * @return the position the photoId was added or -1 if no free positions are available
	 */
	int store(String photoId);
	
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
