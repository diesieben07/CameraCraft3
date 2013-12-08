package de.take_weiland.mods.cameracraft.api;

import de.take_weiland.mods.cameracraft.api.photo.PhotoStorage;

public interface PhotoStorageProvider {

	public static final Object STORAGE_CHANGED_EVENT = new Object();
	
	PhotoStorage getPhotoStorage();
	
}
