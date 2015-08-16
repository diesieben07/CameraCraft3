package de.take_weiland.mods.cameracraft.api.photo;

import java.util.UUID;

/**
 * 
 * Represents fixed information about a Photo.<br>
 * That includes it's unique ID, the Minecraft username of it's owner, and possibly a Location and Time based on the type of camera used.
 * 
 * @author diesieben07
 *
 */
public interface PhotoData {

    long getId();

    UUID getOwner();

    int getX();
	
	int getY();
	
	int getZ();
	
	int getDimension();

    TimeType getTimeType();
	
	long getTime();
	
}
