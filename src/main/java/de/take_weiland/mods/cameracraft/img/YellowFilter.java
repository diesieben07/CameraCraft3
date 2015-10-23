package de.take_weiland.mods.cameracraft.img;

import de.take_weiland.mods.cameracraft.api.img.SimpleRgbFilter;

class YellowFilter implements SimpleRgbFilter {

	@Override
	public int apply(int rgb) {
		return rgb & 0xffff00;
	}

}
