package de.take_weiland.mods.cameracraft.server;

import de.take_weiland.mods.cameracraft.CCProxy;

import java.awt.image.BufferedImage;
import java.io.InputStream;

public class ServerProxy implements CCProxy {

	@Override
	public void preInit() { }

	@Override
	public void handleStandardPhotoRequest(int transferId) { }

	@Override
	public void displayNamePhotoGui(String oldName) { }

	@Override
	public void spawnAlkalineBubbleFX(double x, double y, double z, double motionX, double motionY, double motionZ) { }

	@Override
	public void handleClientPhotoData(long photoId, InputStream in) { }

	@Override
	public void handleClientPhotoData(long photoId, BufferedImage img) { }

	@Override
	public void displayPhotoGui(long photoId, String displayName, boolean canRename) { }

}
