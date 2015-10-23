package de.take_weiland.mods.cameracraft.img;

import static de.take_weiland.mods.cameracraft.img.ImageUtil.clampRgb;
import de.take_weiland.mods.cameracraft.api.img.SimpleRgbFilter;

class OverexposeFilter implements SimpleRgbFilter {

	private static final int FACTOR = 14;

	@Override
	public int apply(int rgb) {
		int r = (rgb >> 16) & 0xff;
    	int g = (rgb >> 8) & 0xff;
    	int b = rgb & 0xff;
    	
    	r = clampRgb(r * FACTOR);
    	g = clampRgb(g * FACTOR);
    	b = clampRgb(b * FACTOR);
    	
    	return r << 16 | g << 8 | b;
	}

}
