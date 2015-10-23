package de.take_weiland.mods.cameracraft.api.camera;

import de.take_weiland.mods.cameracraft.api.img.ImageFilter;
import de.take_weiland.mods.cameracraft.api.photo.PhotoStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * <p>A Camera.</p>
 * <p>Instances of this interface must eventually be disposed using {@link #dispose()}.</p>
 */
public interface Camera {

	boolean OPEN = false;
    boolean CLOSE = true;

	/**
     * <p>Whether this camera can currently take a photo.</p>
	 * @return true if this camera can take a photo
	 */
	boolean canTakePhoto();
	
	/**
     * <p>Whether this camera currently has a storage medium available, such as a memory card or a film.</p>
	 * @return true if this camera has a storage medium available
	 */
	boolean hasStorage();

    /**
     * <p>Whether this camera needs a battery to operate.</p>
     * @return true if this camera needs a battery
     */
	boolean needsBattery();

    /**
     * <p>Whether this camera has a battery available.</p>
     * @return true if this camera has a battery available
     */
	boolean hasBattery();

    /**
     * <p>Get the {@code ItemStack} of the currently inserted battery, if any.</p>
     * @return the current battery ItemStack or null if none
     */
	ItemStack getBattery();

	/**
     * <p>Get the {@code ItemStack} of the currently inserted lens, if any.</p>
	 * @return the current lens ItemStack
	 */
	ItemStack getLens();

	/**
     * <p>Get an ImageFilter combining all the filters that need to be applied when a photo is taken with this camera.
     * This includes the filter for the lens, the filter for the PhotoStorage and possibly any additional filters for this type of camera.</p>
	 * @return the ImageFilter
	 */
	ImageFilter getFilter();

    /**
     * <p>Get the {@code ItemStack} this inventory was created from, if any. If present, the returned ItemStack's Item must implement {@link CameraItem}.</p>
     * @return the associated ItemStack or null if none
     */
    ItemStack getCamera();

	/**
     * <p>Whether the camera has a lid to lock the storage slot. Usually present for film cameras.</p>
	 * @return true if the storage slot can be locked
	 */
	boolean hasLid();

	/**
     * <p>Whether the storage slot lid is currently closed.</p>
	 * @return true if the storage slot can be locked and is currently locked
	 */
	boolean isLidClosed();

    /**
     * <p>Set the state of the storage slot lid, if present.</p>
     * @param close true to close the lid, false to open it
     */
    void setLidState(boolean close);

    /**
     * <p>Toggle the state of the storage slot lid.</p>
     */
    default void toggleLid() {
        setLidState(!isLidClosed());
    }

    /**
     * <p>Whether this camera can rewind it's storage, usually a film.</p>
	 * @return true if the camera can rewind it's storage
	 */
	boolean canRewind();
	
	/**
     * <p>Rewind the currently inserted storage, if possible.</p>
     * @return true on success
	 */
	boolean rewind();

	/**
     * <p>Get the current storage, if any.</p>
	 * @return the current storage or null if none
	 */
	PhotoStorage getPhotoStorage();

    /**
     * <p>Open this camera's GUI for the given player.</p>
     * @param player the player
     */
    void openGui(EntityPlayer player);

	/**
     * <p>Called when a photo is taken with this camera, before the photo is stored in this camera.</p>
	 */
	void onTakePhoto();

	/**
	 * <p>Trigger a photo on this camera, if possible.</p>
     * @return true on success
	 */
    boolean takePhoto();

    /**
     * <p>Dispose this inventory.</p>
	 */
	void dispose();
	
}
