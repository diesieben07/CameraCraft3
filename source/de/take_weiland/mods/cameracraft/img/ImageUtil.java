package de.take_weiland.mods.cameracraft.img;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public final class ImageUtil {

	private ImageUtil() { }
	
	public static BufferedImage fromRawRgb(int width, int height, byte[] data) {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				int i = (x + (width * y)) * 3;
				int r = data[i] & 0xFF;
				int g = data[i + 1] & 0xFF;
				int b = data[i + 2] & 0xFF;
				image.setRGB(x, y, (0xFF << 24) | (r << 16) | (g << 8) | b);
			}
		}
		return image;
	}

	public static BufferedImage scaledCenteredSquareAndRotate(BufferedImage source, int sqSize) {
		int width = source.getWidth();
		int height = source.getHeight();
		
		int xOffset;
		int yOffset;
		if (width > height) {
			xOffset = (width - height) / 2;
			yOffset = 0;
		} else {
			xOffset = 0;
			yOffset = (height - width) / 2;
		}
		
		int xEnd = width - xOffset;
		int yEnd = height - yOffset;
		
		BufferedImage scaled = new BufferedImage(sqSize, sqSize, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = scaled.createGraphics();

		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		g.drawImage(source, 0, 0, sqSize, sqSize, xOffset, yEnd, xEnd, yOffset, null); // coordinates swapped because OpenGL returns flipped image
		
		g.dispose();
		return scaled;
	}
}
