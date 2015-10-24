package de.take_weiland.mods.cameracraft;

import de.take_weiland.mods.cameracraft.network.PacketTakenPhoto;

import java.io.InputStream;
import java.util.concurrent.CompletionStage;

public interface CCProxy {

	default void preInit() { }

	default CompletionStage<PacketTakenPhoto> handleStandardPhotoRequest() {
		throw new IllegalStateException();
	}

	default void handleClientPhotoData(long photoId, InputStream in) {
		throw new IllegalStateException();
	}

	default void displayNamePhotoGui(String oldName) {
		throw new IllegalStateException();
	}

	default void displayPhotoGui(long photoId, String displayName, boolean canRename) {
		throw new IllegalStateException();
	}

	default void spawnAlkalineBubbleFX(double x, double y, double z, double motionX, double motionY, double motionZ) {
		throw new IllegalStateException();
	}

}
