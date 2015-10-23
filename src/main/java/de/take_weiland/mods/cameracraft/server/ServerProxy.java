package de.take_weiland.mods.cameracraft.server;

import de.take_weiland.mods.cameracraft.CCProxy;
import de.take_weiland.mods.cameracraft.network.PacketTakenPhoto;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.concurrent.CompletionStage;

public class ServerProxy implements CCProxy {

	@Override
	public void preInit() { }

	@Override
	public CompletionStage<PacketTakenPhoto> handleStandardPhotoRequest() { }

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
