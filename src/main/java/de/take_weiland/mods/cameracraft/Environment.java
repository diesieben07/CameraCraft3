package de.take_weiland.mods.cameracraft;

import java.io.InputStream;

public interface Environment {

	void preInit();

	void onPhotoRequest(int transferId);

	void handleClientPhotoData(String photoId, InputStream in);
	
	void displayNamePhotoGui(String oldName);
	
	void displayPhotoGui(String photoId, String displayName, boolean canRename);
	
	void spawnAlkalineBubbleFX(double x, double y, double z, double motionX, double motionY, double motionZ);
	
}
