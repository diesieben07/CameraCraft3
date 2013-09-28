package de.take_weiland.mods.cameracraft.img;

import java.awt.image.BufferedImage;

import de.take_weiland.mods.cameracraft.api.img.ImageFilter;

public abstract class AbstractImageFilter implements ImageFilter {

	@Override
	public BufferedImage apply(BufferedImage src) {
		int w = src.getWidth();
	    int h = src.getHeight();
		
	    int[] rgbArr = src.getRGB(0, 0, w, h, null, 0, w);
	    
	    int len = rgbArr.length;
	    for (int i = 0; i < len; ++i) {
	    	rgbArr[i] = modifiyRgb(rgbArr[i]);
	    }
	    
	    src.setRGB(0, 0, w, h, rgbArr, 0, w);
	    return src;
	}
	
	protected abstract int modifiyRgb(int rgb);

}
