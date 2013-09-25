package de.take_weiland.mods.cameracraft;

import java.awt.image.BufferedImage;

public interface Environment {

	void preInit();

	void executePhoto();

	void handleClientPhotoData(BufferedImage img);
	
	void displayNamePhotoGui(String oldName);
	
}
