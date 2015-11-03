package de.take_weiland.mods.cameracraft.img;

import de.take_weiland.mods.cameracraft.api.img.SimpleRgbFilter;

class ColorFilter implements SimpleRgbFilter {

    enum Channel {RED, GREEN, BLUE}

    private final int blendRgb;

    public ColorFilter(int color) {
        blendRgb = color;
    }

    @Override
    public int apply(int rgb) {
        for (int shift = 0; shift <= 16; shift += 8) {
            int base = (rgb & 0xff << shift) >> shift;
            int tint = (blendRgb & 0xff << shift) >> shift;

            int result = (int) ((tint - base) * 0.5 + base);

            rgb = rgb & ~(0xff << shift) | result << shift;
        }
        return rgb;

//		int r = rgb >> 16 & 0xff;
//		int g = rgb >> 8 & 0xff;
//		int b = rgb & 0xff;
//		int luminance = (int) (r * 0.3f + g * 0.59f + b * 0.11f);
//
//		int color = (int) (luminance * 2.4f);
//		if (color > 255) color = 255;
//
//		return color << shift;
    }

}
