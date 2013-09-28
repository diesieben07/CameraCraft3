package de.take_weiland.mods.cameracraft.img;

import java.awt.image.BufferedImage;

import com.google.common.collect.ObjectArrays;

import de.take_weiland.mods.cameracraft.api.img.ImageFilter;
import de.take_weiland.mods.cameracraft.api.img.SimpleRgbFilter;

class ImageFilterFromRGBChained implements ImageFilter {
	
	final SimpleRgbFilter[] filters;

	public ImageFilterFromRGBChained(SimpleRgbFilter... filters) {
		this.filters = filters;
	}

	@Override
	public BufferedImage apply(BufferedImage src) {
		return ImageFilters.apply(src, filters);
	}

	@Override
	public ImageFilter combine(ImageFilter other) {
		if (other instanceof ImageFilterFromRGBChained) {
			return new ImageFilterFromRGBChained(ObjectArrays.concat(this.filters, ((ImageFilterFromRGBChained) other).filters, SimpleRgbFilter.class));
		} else if (other instanceof ImageFilterFromRGB) {
			return new ImageFilterFromRGBChained(ObjectArrays.concat(this.filters, ((ImageFilterFromRGB) other).filter));
		} else {
			return new ChainedImageFilter(this, other);
		}
	}
}