package de.take_weiland.mods.cameracraft.img;

import de.take_weiland.mods.cameracraft.api.img.SimpleRgbFilter;

import static de.take_weiland.mods.cameracraft.img.ImageUtil.clampRgb;

class OverexposeFilter implements SimpleRgbFilter {

	private static final int FACTOR = 12;

	@Override
	public int apply(int rgb) {
		int r = rgb >> 16 & 0xff;
    	int g = rgb >> 8 & 0xff;
    	int b = rgb & 0xff;
    	
    	r = modRGB(r);
    	g = modRGB(g);
        b = modRGB(b);

        return r << 16 | g << 8 | b;
	}

    private static int modRGB(int v) {
        return clampRgb(Math.max(v * FACTOR, 200));
    }

}
