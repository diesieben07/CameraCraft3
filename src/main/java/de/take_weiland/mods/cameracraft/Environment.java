package de.take_weiland.mods.cameracraft;

import java.awt.image.BufferedImage;
import java.io.InputStream;

public interface Environment {

	void preInit();

	void handleStandardPhotoRequest(int transferId);

	void handleClientPhotoData(long photoId, InputStream in);

	void handleClientPhotoData(long photoId, BufferedImage img);

	void displayNamePhotoGui(String oldName);
	
	void displayPhotoGui(long photoId, String displayName, boolean canRename);
	
	void spawnAlkalineBubbleFX(double x, double y, double z, double motionX, double motionY, double motionZ);
	
}
