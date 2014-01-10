package de.take_weiland.mods.cameracraft.server;

import java.io.InputStream;

import de.take_weiland.mods.cameracraft.Environment;

public class EnvironmentServer implements Environment {

	@Override
	public void preInit() { }

	@Override
	public void onPhotoRequest(int transferId) { }

	@Override
	public void displayNamePhotoGui(String oldName) { }

	@Override
	public void spawnAlkalineBubbleFX(double x, double y, double z, double motionX, double motionY, double motionZ) { }

	@Override
	public void handleClientPhotoData(String photoId, InputStream in) { }

	@Override
	public void displayPhotoGui(String photoId) { }

}
