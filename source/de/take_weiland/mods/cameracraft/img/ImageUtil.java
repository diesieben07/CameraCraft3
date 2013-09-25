package de.take_weiland.mods.cameracraft.img;

import java.awt.image.BufferedImage;

public final class ImageUtil {

	private ImageUtil() { }
	
	public static BufferedImage fromRawRotatedRgb(int width, int height, byte[] data) {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				int i = (x + (width * y)) * 3;
				int r = data[i] & 0xFF;
				int g = data[i + 1] & 0xFF;
				int b = data[i + 2] & 0xFF;
				image.setRGB(x, height - y - 1, (0xFF << 24) | (r << 16) | (g << 8) | b);
			}
		}
		return image;
	}
}
