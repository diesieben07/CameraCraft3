package de.take_weiland.mods.cameracraft.client;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import de.take_weiland.mods.cameracraft.CameraCraft;

public class PhotoDataCache {

	private static final LoadingCache<String, CacheElement> cache;
	
	static {
		cache = CacheBuilder.newBuilder().concurrencyLevel(CameraCraft.maxThreadCount + 1).expireAfterAccess(1, TimeUnit.MINUTES).build(new PhotoDataLoader());
	}
	
	private PhotoDataCache() { }
	
	static void flushCache() {
		cache.invalidateAll();
	}
	
	public static BufferedImage getTexture(String photoId) {
		return cache.getUnchecked(photoId).img;
	}
	
	static void injectReceivedPhoto(String photoId, InputStream in) throws IOException {
		CacheElement element = cache.getIfPresent(photoId);
		if (element != null) {
			element.img = ImageIO.read(in);
		}
	}
	
	static class CacheElement {
		
		BufferedImage img;
		
	}
	
	static class PhotoDataLoader extends CacheLoader<String, CacheElement> {

		@Override
		public CacheElement load(String key) throws Exception {
			return new CacheElement();
		}
		
	}

}
