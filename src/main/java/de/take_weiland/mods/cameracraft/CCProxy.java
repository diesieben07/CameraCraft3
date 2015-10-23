package de.take_weiland.mods.cameracraft;

import de.take_weiland.mods.cameracraft.network.PacketTakenPhoto;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.concurrent.CompletionStage;

public interface CCProxy {

	void preInit();

	CompletionStage<PacketTakenPhoto> handleStandardPhotoRequest();

	void handleClientPhotoData(long photoId, InputStream in);

	void handleClientPhotoData(long photoId, BufferedImage img);

	void displayNamePhotoGui(String oldName);
	
	void displayPhotoGui(long photoId, String displayName, boolean canRename);
	
	void spawnAlkalineBubbleFX(double x, double y, double z, double motionX, double motionY, double motionZ);
	
}
