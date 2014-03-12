package de.take_weiland.mods.cameracraft.img;

import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.cameracraft.api.img.ImageFilter;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public final class ImageUtil {

	private ImageUtil() { }
	
	public static BufferedImage fromRawRotatedRgb(int width, int height, byte[] data) {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int i = (x + (width * y)) * 3;
				int r = data[i] & 0xFF;
				int g = data[i + 1] & 0xFF;
				int b = data[i + 2] & 0xFF;
				image.setRGB(x, height - y - 1, (0xFF << 24) | (r << 16) | (g << 8) | b);
			}
		}
		return image;
	}
	
	public static int clampRgb(int value) {
		return MathHelper.clamp_int(value, 0, 255);
	}
	
	public static void savePngAsync(final BufferedImage img, final File file) {
		savePngAsync(img, file, null);
	}
	
	public static void savePngAsync(final BufferedImage img, final File file, final ImageFilter filter) {
		CameraCraft.executor.execute(new Runnable() {
			
			@Override
			public void run() {
				try {
					ImageIO.write(filter == null ? img : filter.apply(img), "PNG", file);
				} catch (IOException e) {
					CrashReport cr = CrashReport.makeCrashReport(e, "Saving ImageFile");
					cr.makeCategory("File being saved to").addCrashSection("Location", file);
					throw new ReportedException(cr);
				}
			}
		});
	}
}
