package de.take_weiland.mods.cameracraft.client;

import java.awt.image.BufferedImage;

import de.take_weiland.mods.cameracraft.img.ImageUtil;
import de.take_weiland.mods.cameracraft.network.PacketPhotoData;

public final class ScreenshotPostProcess implements Runnable {

	private static final int SIZE = 256;
	
	private byte[] data;
	private final int width;
	private final int height;
	
	public ScreenshotPostProcess(int width, int height, byte[] data) {
		this.data = data;
		this.width = width;
		this.height = height;
	}

	@Override
	public void run() {
		int width = this.width;
		int height = this.height;
		BufferedImage image = ImageUtil.fromRawRgb(width, height, data);
		data = null; // for GC
		
		image = ImageUtil.scaledCenteredSquareAndRotate(image, SIZE);
		
		new PacketPhotoData(image).sendToServer();
	}

}
