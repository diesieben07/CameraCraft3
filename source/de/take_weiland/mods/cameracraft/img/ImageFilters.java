package de.take_weiland.mods.cameracraft.img;

import java.awt.image.BufferedImage;

import de.take_weiland.mods.cameracraft.api.img.ImageFilter;
import de.take_weiland.mods.cameracraft.api.img.SimpleRgbFilter;
import de.take_weiland.mods.cameracraft.img.ColorFilter.Channel;

public final class ImageFilters {

	private ImageFilters() { }
	
	public static final ImageFilter RED = fromRgbFilter(new ColorFilter(Channel.RED));
	public static final ImageFilter GREEN = fromRgbFilter(new ColorFilter(Channel.GREEN));
	public static final ImageFilter BLUE = fromRgbFilter(new ColorFilter(Channel.BLUE));
	public static final ImageFilter SEPIA = fromRgbFilter(new SepiaFilter());
	public static final ImageFilter GRAY = fromRgbFilter(new GrayscaleFilter());
	public static final ImageFilter OVEREXPOSE = fromRgbFilter(new OverexposeFilter());
	
	public static BufferedImage apply(BufferedImage src, SimpleRgbFilter filter) {
		int w = src.getWidth();
	    int h = src.getHeight();
		
	    int[] rgbArr = src.getRGB(0, 0, w, h, null, 0, w);
	    
	    int len = rgbArr.length;
	    for (int i = 0; i < len; ++i) {
	    	rgbArr[i] = filter.modifiyRgb(rgbArr[i]);
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
	    		rgb = filter.modifiyRgb(rgb);
	    	}
	    	rgbArr[i] = rgb;
	    }
	    
	    src.setRGB(0, 0, w, h, rgbArr, 0, w);
	    return src;
	}
	
	public static ImageFilter fromRgbFilter(final SimpleRgbFilter filter) {
		return new ImageFilterFromRGB(filter);
	}
	
	public static ImageFilter fromRgbFilters(final SimpleRgbFilter... filters) {
		return new ImageFilterFromRGBMultiple(filters);
	}
	
	public static ImageFilter combine(ImageFilter... filters) {
		ImageFilter result = filters[0];
		int len = filters.length;
		if (len == 1) {
			return result;
		}
		for (int i = 1; i < len; ++i) {
			result = result.combine(filters[i]);
		}
		return result;
	}
	
}
