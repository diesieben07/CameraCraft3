package de.take_weiland.mods.cameracraft;

import java.awt.image.BufferedImage;

public interface Environment {

	void setup();

	void executePhoto();

	void handleClientPhotoData(BufferedImage img);
	
}
