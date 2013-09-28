package de.take_weiland.mods.cameracraft.img;

import de.take_weiland.mods.cameracraft.api.img.SimpleRgbFilter;

class ColorFilter implements SimpleRgbFilter {

	static enum Channel { RED, GREEN, BLUE }
	
	private int shift;
	
	public ColorFilter(Channel c) {
		switch (c) {
		case RED:
			shift = 16;
			break;
		case GREEN:
			shift = 8;
			break;
		default:
			shift = 0;
			break;
		}
	}
	
	@Override
	public int modifiyRgb(int rgb) {
		int r = (rgb >> 16) & 0xff;
		int g = (rgb >> 8) & 0xff;
		int b = rgb & 0xff;
		int luminance = (int) (r * 0.3f + g * 0.59f + b * 0.11f);
		
		int color = (int) (luminance * 2.4f);
		if (color > 255) color = 255;

		return color << shift;
	}

}
