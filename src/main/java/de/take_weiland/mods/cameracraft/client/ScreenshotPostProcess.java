package de.take_weiland.mods.cameracraft.client;

import java.awt.image.BufferedImage;

import de.take_weiland.mods.cameracraft.img.ImageUtil;
import de.take_weiland.mods.cameracraft.network.PacketTakenPhoto;

public final class ScreenshotPostProcess implements Runnable {

	private final int transferId;
	private byte[] data;
	private final int width;
	private final int height;
	
	public ScreenshotPostProcess(int transferId, int width, int height, byte[] data) {
		this.transferId = transferId;
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
		
		new PacketTakenPhoto(transferId, image).sendToServer();
	}

}
