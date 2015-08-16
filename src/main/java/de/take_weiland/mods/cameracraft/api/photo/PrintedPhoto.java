package de.take_weiland.mods.cameracraft.api.photo;

/**
 * 
 * Represents in a printed form.<br>
 * Calling any of the inherited methods (except {@link PhotoData#getId() getId()}) might trigger loading the actual data from disk,
 * as the printed photo ItemStack only contains the data returned by the methods in this Interface. 
 * 
 * @author Take Weiland
 *
 */
public interface PrintedPhoto {

	long getPhotoId();

	/**
	 * @return the width of this photo, in blocks
	 */
	int getWidth();
	
	/**
	 * @return the height of this photo, in blocks
	 */
	int getHeight();
	
	/**
	 * @return the name of this photo
	 */
	String getName();
	
}
