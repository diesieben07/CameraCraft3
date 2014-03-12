package de.take_weiland.mods.cameracraft.server;

import de.take_weiland.mods.cameracraft.Environment;

import java.io.InputStream;

public class EnvironmentServer implements Environment {

	@Override
	public void preInit() { }

	@Override
	public void handleStandardPhotoRequest(int transferId) { }

	@Override
	public void displayNamePhotoGui(String oldName) { }

	@Override
	public void spawnAlkalineBubbleFX(double x, double y, double z, double motionX, double motionY, double motionZ) { }

	@Override
	public void handleClientPhotoData(Integer photoId, InputStream in) { }

	@Override
	public void displayPhotoGui(Integer photoId, String displayName, boolean canRename) { }

}
