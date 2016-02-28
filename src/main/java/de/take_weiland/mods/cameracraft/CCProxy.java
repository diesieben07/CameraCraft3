package de.take_weiland.mods.cameracraft;

import de.take_weiland.mods.cameracraft.network.PacketImageResponse;

import java.io.InputStream;
import java.util.concurrent.CompletionStage;

public interface CCProxy {

	default void preInit() { }

	default CompletionStage<PacketImageResponse> handleStandardPhotoRequest() {
		throw new IllegalStateException();
	}

    default CompletionStage<PacketImageResponse> handleViewportPhoto(int viewportId) {
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

	default int getProcessorRenderId() {
		throw new IllegalStateException();
	}

    default void newViewport(int id, int dimension, double x, double y, double z, float pitch, float yaw) {
        throw new IllegalStateException();
    }

	default void killViewport(int id) {
        throw new IllegalStateException();
    }
}
