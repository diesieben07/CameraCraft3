package de.take_weiland.mods.cameracraft.api.camera;

import de.take_weiland.mods.cameracraft.api.photo.PhotoStorage;
import net.minecraft.inventory.IInventory;

public interface Camera extends IInventory {

	PhotoStorage getPhotoStorage();
	
	CameraType getType();
	
}
