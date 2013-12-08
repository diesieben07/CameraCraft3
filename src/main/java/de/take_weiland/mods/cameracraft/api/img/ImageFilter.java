package de.take_weiland.mods.cameracraft.api.img;

import java.awt.image.BufferedImage;

public interface ImageFilter {

	BufferedImage apply(BufferedImage image);
	
	ImageFilter combine(ImageFilter other);
	
}
