package de.take_weiland.mods.cameracraft.img;

import de.take_weiland.mods.cameracraft.api.img.ImageFilter;
import de.take_weiland.mods.cameracraft.api.img.SimpleRgbFilter;
import de.take_weiland.mods.cameracraft.img.ColorFilter.Channel;

import java.awt.image.BufferedImage;

public final class ImageFilters {

	private ImageFilters() { }
	
	public static final ImageFilter NO_FILTER = new ImageFilter() {
		
		@Override
		public ImageFilter combine(ImageFilter other) {
			return other;
		}
		
		@Override
		public BufferedImage apply(BufferedImage image) {
			return image;
		}
	};
	
	public static final ImageFilter RED = new ColorFilter(Channel.RED);
    public static final ImageFilter GREEN = new ColorFilter(Channel.GREEN);
    public static final ImageFilter BLUE = new ColorFilter(Channel.BLUE);
    public static final ImageFilter YELLOW = new YellowFilter();

    public static final ImageFilter SEPIA = new SepiaFilter();
    public static final ImageFilter GRAY = new GrayscaleFilter();
    public static final ImageFilter OVEREXPOSE = new OverexposeFilter();

    public static BufferedImage apply(BufferedImage src, SimpleRgbFilter filter) {
		int w = src.getWidth();
	    int h = src.getHeight();
		
	    int[] rgbArr = src.getRGB(0, 0, w, h, null, 0, w);
	    
	    int len = rgbArr.length;
	    for (int i = 0; i < len; ++i) {
	    	rgbArr[i] = filter.apply(rgbArr[i]);
	    }
	    
	    src.setRGB(0, 0, w, h, rgbArr, 0, w);
	    return src;
	}
	
	public static BufferedImage apply(BufferedImage src, SimpleRgbFilter... filters) {
		int w = src.getWidth();
	    int h = src.getHeight();
		
	    int[] rgbArr = src.getRGB(0, 0, w, h, null, 0, w);
	    
	    int len = rgbArr.length;
	    for (int i = 0; i < len; ++i) {
	    	int rgb = rgbArr[i];
	    	for (SimpleRgbFilter filter : filters) {
	    		rgb = filter.apply(rgb);
	    	}
	    	rgbArr[i] = rgb;
	    }
	    
	    src.setRGB(0, 0, w, h, rgbArr, 0, w);
	    return src;
	}

    public static ImageFilter combine(ImageFilter a, ImageFilter b) {
        if (a == null) {
            return b;
        } else if (b == null) {
            return a;
        } else {
            return a.combine(b);
        }
	}
	
	public static ImageFilter combine(ImageFilter... filters) {
		ImageFilter result = null;
        for (ImageFilter current : filters) {
            if (current != null) {
                if (result == null) {
                    result = current;
                } else {
                    result = result.combine(current);
                }
            }
        }
		return result == null ? NO_FILTER : result;
	}
}
