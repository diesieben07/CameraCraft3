package de.take_weiland.mods.cameracraft.api.photo;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.UUID;

/**
 * 
 * Represents fixed information about a Photo.<br>
 * that includes it's unique id, the UUID of it's owner, and possibly a location and time based on the type of camera used.
 * 
 * @author diesieben07
 *
 */
public interface Photo {

    long getId();

    /**
     * <p>Get this photo as a BufferedImage. This method might need to load data from disk in which case it will block.</p>
     * @return the BufferedImage
     */
    BufferedImage getImage() throws IOException;

    UUID getOwner();

    int getX();
	
	int getY();
	
	int getZ();
	
	int getDimension();

    long getTime();
	
}
