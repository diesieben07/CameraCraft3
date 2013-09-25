package de.take_weiland.mods.cameracraft.api.camera;


/**
 * Represents something that can store photos
 * @author Take Weiland
 *
 */
public interface PhotoStorage {

	/**
	 * @return the amount of photos this PhotoStorage can store
	 */
	int capacity();
	
	/**
	 * @return the number of photos this PhotoStorage currently has stored
	 */
	int size();
	
	boolean isFull();
	
	/**
	 * gets the photoId at the given positionIndex
	 * @param position
	 * @return
	 */
	String get(int position);
	
	/**
	 * sets the first free slot to the given photoId 
	 * @param photoId
	 * @return the position the photoId was added or -1 if no free positions are available
	 */
	int store(String photoId);
	
	void remove(int position);
	
	int getPosition(String photoId);
	
	boolean isSealed();
	
	
//	
//	/**
//	 * stores the given photo in this PhotoStorage
//	 * @param photo
//	 * @return if the photo was stored successfully (might fail if this storage is full)
//	 */
//	boolean store(Photo photo);
//	
//	/**
//	 * get a list of photoIds this storage contains<br>
//	 * this is an immutable view
//	 * @return
//	 */
//	List<String> getPhotosRaw();
	
//	/**
//	 * gets a list of the photos this storage contains<br>
//	 * this is an immutable view
//	 * @return
//	 */
//	List<Photo> getPhotos();
	
}
