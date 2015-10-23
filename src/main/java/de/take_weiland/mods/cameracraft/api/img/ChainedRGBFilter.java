package de.take_weiland.mods.cameracraft.api.img;

import com.google.common.collect.ObjectArrays;

final class ChainedRGBFilter implements SimpleRgbFilter {
	
	final SimpleRgbFilter[] filters;

	ChainedRGBFilter(SimpleRgbFilter... filters) {
		this.filters = filters;
	}

    @Override
    public int apply(int rgb) {
        for (SimpleRgbFilter filter : filters) {
            rgb = filter.apply(rgb);
        }
        return rgb;
    }

    @Override
	public ImageFilter combine(ImageFilter other) {
		if (other instanceof ChainedRGBFilter) {
			return new ChainedRGBFilter(ObjectArrays.concat(this.filters, ((ChainedRGBFilter) other).filters, SimpleRgbFilter.class));
		} else if (other instanceof SimpleRgbFilter) {
			return new ChainedRGBFilter(ObjectArrays.concat(this.filters, (SimpleRgbFilter) other));
		} else {
			return new ChainedImageFilter(this, other);
		}
	}
}