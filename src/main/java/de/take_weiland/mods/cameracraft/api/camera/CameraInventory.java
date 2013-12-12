package de.take_weiland.mods.cameracraft.api.camera;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import de.take_weiland.mods.cameracraft.api.img.ImageFilter;
import de.take_weiland.mods.cameracraft.api.photo.PhotoStorage;

public interface CameraInventory extends IInventory {

	/**
	 * if this camera is currently able to take a photo
	 * @return
	 */
	boolean canTakePhoto();
	
	/**
	 * get if this camera currently has a PhotoStorage available
	 * @return
	 */
	boolean hasStorage();
	
	boolean needsBattery();
	
	boolean hasBattery();
	
	ItemStack getBattery();

	/**
	 * get the ItemStack containing the CameraItem this camera was created from
	 * @return
	 */
	ItemStack getCamera();

	/**
	 * get the ItemStack holding the LensItem for this camera. May be null if none
	 * @return
	 */
	ItemStack getLens();

	/**
	 * get an ImageFilter combining all the filters that should be applied when a photo is taken with this camera.<br>
	 * That includes the filter by the lens, the filter by the PhotoStorage and possibly any additional filters for this type of camera
	 * @return
	 */
	ImageFilter getFilter();

	/**
	 * @return whether the PhotoStorage slot of this camera is lockable
	 */
	boolean hasLid();

	/**
	 * @return whether the PhotoStorage slot of this camera is currently closed (undefined if <code>{@link #hasLid()} == false</code>)
	 */
	boolean isLidClosed();
	
	/**
	 * toggle the state of the PhotoStorage slot lid
	 */
	void toggleLid();
	
	/**
	 * return whether this camera can rewind it's PhotoStorage (usually a film)
	 * @return
	 */
	boolean canRewind();
	
	/**
	 * rewinds the film on this camera, does nothing if {@link #canRewind()} is false
	 */
	void rewind();

	/**
	 * gets the photo storage this camera uses. May be null if currently no storage
	 * @return
	 */
	PhotoStorage getPhotoStorage();
	
	Container createContainer(EntityPlayer player);

	/**
	 * call this when a photo is taken, before the photo is requested from the client or stored into the PhotoStorage
	 */
	void onTakePhoto();
	
	/**
	 * call this when you are done working with this camera to clean up resources
	 */
	void dispose();
	
}
