package de.take_weiland.mods.cameracraft.api.camera;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

public interface CameraItem {

	/**
	 * create a new Camera from the given slot in the Inventory
	 * @param stack
	 * @return
	 */
	CameraInventory getInventory(IInventory inventory, int slot);
	
	
	/**
	 * create a new Camera bound to the player's current item
	 * @param player
	 * @return
	 */
	CameraInventory getInventory(EntityPlayer player);
	
}
