package de.take_weiland.mods.cameracraft.img;

import java.awt.image.BufferedImage;

import com.google.common.collect.ObjectArrays;

import de.take_weiland.mods.cameracraft.api.img.ImageFilter;
import de.take_weiland.mods.cameracraft.api.img.SimpleRgbFilter;

public class ImageFilterFromRGB implements ImageFilter {
	
	private final SimpleRgbFilter filter;

	ImageFilterFromRGB(SimpleRgbFilter filter) {
		this.filter = filter;
	}

	@Override
	public BufferedImage apply(BufferedImage src) {
		return ImageFilters.apply(src, filter);
	}

	@Override
	public ImageFilter combine(ImageFilter other) {
		if (other instanceof ImageFilterFromRGB) {
			return new ImageFilterFromRGBMultiple(this.filter, ((ImageFilterFromRGB) other).filter);
		} else if (other instanceof ImageFilterFromRGBMultiple) {
			return new ImageFilterFromRGBMultiple(ObjectArrays.concat(this.filter, ((ImageFilterFromRGBMultiple) other).filters));
		} else {
			return new ChainedImageFilter(this, other);
		}
	}
}