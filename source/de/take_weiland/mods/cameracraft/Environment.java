package de.take_weiland.mods.cameracraft;

import java.awt.image.BufferedImage;

public interface Environment {

	void preInit();

	void onPhotoRequest(int transferId);

	void handleClientPhotoData(BufferedImage img);
	
	void displayNamePhotoGui(String oldName);
	
	void spawnAlkalineBubbleFX(double x, double y, double z, double motionX, double motionY, double motionZ);
	
}
