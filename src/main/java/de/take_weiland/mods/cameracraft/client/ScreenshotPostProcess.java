package de.take_weiland.mods.cameracraft.client;

import de.take_weiland.mods.cameracraft.img.ImageUtil;
import de.take_weiland.mods.cameracraft.network.PacketTakenPhoto;

import java.awt.image.BufferedImage;
import java.util.concurrent.CompletableFuture;

public final class ScreenshotPostProcess implements Runnable {

    private final CompletableFuture<PacketTakenPhoto> future;
    private byte[] data;
	private final int width;
	private final int height;
	
	public ScreenshotPostProcess(CompletableFuture<PacketTakenPhoto> future, int width, int height, byte[] data) {
        this.future = future;
        this.data = data;
		this.width = width;
		this.height = height;
	}

	@Override
	public void run() {
		int width = this.width;
		int height = this.height;
		BufferedImage image = ImageUtil.fromRawRotatedRgb(width, height, data);
		data = null; // for GC

		future.complete(new PacketTakenPhoto(image));
	}

}
