package de.take_weiland.mods.cameracraft.api.camera;

import net.minecraft.inventory.IInventory;

public interface Camera extends IInventory {

	PhotoStorage getPhotoStorage();
	
	CameraType getType();
	
}
