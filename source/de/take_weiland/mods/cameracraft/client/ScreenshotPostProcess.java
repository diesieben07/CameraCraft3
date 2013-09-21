package de.take_weiland.mods.cameracraft.client;

import java.awt.image.BufferedImage;
import java.util.concurrent.CountDownLatch;

import com.google.common.base.Throwables;

import de.take_weiland.mods.cameracraft.img.ImageUtil;
import de.take_weiland.mods.cameracraft.network.PacketTakenPhoto;

public final class ScreenshotPostProcess implements Runnable {

	private static final int SIZE = 256;
	
	private byte[] data;
	private final int width;
	private final int height;
	
	private volatile String photoName;
	
	private final CountDownLatch latch = new CountDownLatch(1);
	
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
		
		try {
			latch.await();
		} catch (InterruptedException e) {
			Throwables.propagate(e);
		}
		
		if (photoName != null) {
			new PacketTakenPhoto(image, photoName).sendToServer();
			System.out.println("sending");
		} else {
			System.out.println("discarding");
		}
	}
	
	public void setPhotoName(String photoName) {
		this.photoName = photoName;
		latch.countDown();
	}

}
