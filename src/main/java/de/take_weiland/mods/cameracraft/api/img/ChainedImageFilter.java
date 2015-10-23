package de.take_weiland.mods.cameracraft.api.img;

import com.google.common.collect.ObjectArrays;

import java.awt.image.BufferedImage;

final class ChainedImageFilter implements ImageFilter {

	private final ImageFilter[] filters;
	
	ChainedImageFilter(ImageFilter... filters) {
		this.filters = filters;
	}

	@Override
	public BufferedImage apply(BufferedImage image) {
		for (ImageFilter filter : filters) {
			image = filter.apply(image);
		}
		return image;
	}

	@Override
	public ImageFilter combine(ImageFilter other) {
		if (other instanceof ChainedImageFilter) {
			return new ChainedImageFilter(ObjectArrays.concat(this.filters, ((ChainedImageFilter) other).filters, ImageFilter.class));
		} else {
			return new ChainedImageFilter(ObjectArrays.concat(this.filters, other));
		}
	}

}
