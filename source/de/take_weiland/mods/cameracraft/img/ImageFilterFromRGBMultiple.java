package de.take_weiland.mods.cameracraft.img;

import java.awt.image.BufferedImage;

import com.google.common.collect.ObjectArrays;

import de.take_weiland.mods.cameracraft.api.img.ImageFilter;
import de.take_weiland.mods.cameracraft.api.img.SimpleRgbFilter;

public class ImageFilterFromRGBMultiple implements ImageFilter {
	
	final SimpleRgbFilter[] filters;

	public ImageFilterFromRGBMultiple(SimpleRgbFilter... filters) {
		this.filters = filters;
	}

	@Override
	public BufferedImage apply(BufferedImage src) {
		return ImageFilters.apply(src, filters);
	}

	@Override
	public ImageFilter combine(ImageFilter other) {
		if (other instanceof ImageFilterFromRGBMultiple) {
			return new ImageFilterFromRGBMultiple(ObjectArrays.concat(this.filters, ((ImageFilterFromRGBMultiple) other).filters, SimpleRgbFilter.class));
		} else {
			return new ChainedImageFilter(this, other);
		}
	}
}