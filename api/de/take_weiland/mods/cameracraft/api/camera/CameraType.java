package de.take_weiland.mods.cameracraft.api.camera;

import de.take_weiland.mods.cameracraft.api.ItemType;

public interface CameraType extends ItemType<CameraType> {

	int getSlots();
	
	boolean isDigital();
	
	boolean isFilm();
	
}
