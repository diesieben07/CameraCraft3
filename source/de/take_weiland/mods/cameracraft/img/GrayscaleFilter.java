package de.take_weiland.mods.cameracraft.img;

import de.take_weiland.mods.cameracraft.api.img.SimpleRgbFilter;


public class GrayscaleFilter implements SimpleRgbFilter {

	// algorithm from http://spyrestudios.com/html5-canvas-image-effects-black-white/
	@Override
	public int modifiyRgb(int rgb) {
		int r = (rgb >> 16) & 0xff;
    	int g = (rgb >> 8) & 0xff;
    	int b = rgb & 0xff;
    	
    	int luminance = (int) (r * 0.3f + g * 0.59f + b * 0.11f);
		
		return luminance << 16 | luminance << 8 | luminance;
	}

}
