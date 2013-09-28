package de.take_weiland.mods.cameracraft.img;

import de.take_weiland.mods.cameracraft.api.img.SimpleRgbFilter;

class SepiaFilter implements SimpleRgbFilter {

	private static final int sepiaIntensity = 20;

	// Play around with this. 20 works well and was recommended
    // by another developer. 0 produces black/white image
	private static final int sepiaDepth = 20;
	
	// modified from http://stackoverflow.com/questions/5132015/how-to-convert-image-to-sepia-in-java
	@Override
	public int modifiyRgb(int rgb) {
		int r = (rgb >> 16) & 0xff;
    	int g = (rgb >> 8) & 0xff;
    	int b = rgb & 0xff;
    	
        int gry = (r + g + b) / 3;
        r = g = b = gry;
        r = r + (sepiaDepth << 1);
        g = g + sepiaDepth;

        if (r>255) r=255;
        if (g>255) g=255;
        if (b>255) b=255;

        // Darken blue color to increase sepia effect
        b-= sepiaIntensity;

        // normalize if out of bounds
        if (b<0) b=0;
        if (b>255) b=255;
        return r << 16 | g << 8 | b;
        
	}
}
