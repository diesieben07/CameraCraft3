package de.take_weiland.mods.cameracraft.api.camera;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;


/**
 * 
 * Represents fixed information about a Photo.<br>
 * That includes it's unique ID, the Minecraft username of it's owner, and possibly a Location and Time based on the type of camera used.
 * 
 * @author Take Weiland
 *
 */
public interface Photo {

	String getId();
	
	String getOwner();
	
	EntityPlayer getPlayerOwner();
	
	boolean hasLocationAndTime();
	
	int getX();
	
	int getY();
	
	int getZ();
	
	int getDimension();
	
	World getWorld();
	
	TimeType getTimeType();
	
	long getTime();
	
}
