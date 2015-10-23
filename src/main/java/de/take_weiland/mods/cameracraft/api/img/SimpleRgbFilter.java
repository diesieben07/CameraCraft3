package de.take_weiland.mods.cameracraft.api.img;

import com.google.common.collect.ObjectArrays;
import de.take_weiland.mods.cameracraft.img.ImageFilters;

import java.awt.image.BufferedImage;

public interface SimpleRgbFilter extends ImageFilter {

	int apply(int rgb);

    @Override
    default BufferedImage apply(BufferedImage image) {
        return ImageFilters.apply(image, this);
    }

    @Override
    default ImageFilter combine(ImageFilter other) {
        if (other instanceof ChainedRGBFilter) {
            return new ChainedRGBFilter(ObjectArrays.concat(this, ((ChainedRGBFilter) other).filters));
        } else if (other instanceof SimpleRgbFilter) {
            return new ChainedRGBFilter(this, (SimpleRgbFilter) other);
        } else {
            return new ChainedImageFilter(this, other);
        }
    }
}
