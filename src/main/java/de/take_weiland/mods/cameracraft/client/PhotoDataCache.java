package de.take_weiland.mods.cameracraft.client;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalNotification;
import de.take_weiland.mods.cameracraft.network.PacketClientRequestPhoto;
import de.take_weiland.mods.commons.client.Rendering;
import de.take_weiland.mods.commons.util.Scheduler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

@ParametersAreNonnullByDefault
public class PhotoDataCache {

	static final Minecraft mc = Minecraft.getMinecraft();
	private static final LoadingCache<Long, CacheElement> cache;
	
	static {
		cache = CacheBuilder.newBuilder()
				.concurrencyLevel(2) // everything higher is unlikely to ever happen
				.expireAfterAccess(3, TimeUnit.MINUTES) // TODO figure out the right value here
				.removalListener(PhotoDataCache::onCacheRemove)
				.build(new PhotoDataLoader());
	}
	
	private PhotoDataCache() { }

    private static void onCacheRemove(RemovalNotification<Long, CacheElement> notification) {
        CacheElement cacheElement = notification.getValue();
        if (cacheElement != null) {
            ResourceLocation location = cacheElement.loc;
            if (location != null) {
                Scheduler.client().execute(() -> Rendering.unloadTexture(location));
            }
        }
    }

	static void invalidate() {
		cache.invalidateAll();
	}
	
	public static void bindTexture(long photoId) {
		cache.getUnchecked(photoId).bindTexture();
	}

	public static CacheElement get(long photoId) {
		return cache.getUnchecked(photoId);
	}
	
	static void injectReceivedPhoto(Integer photoId, InputStream in) throws IOException {
		CacheElement element = cache.getIfPresent(photoId);
		if (element != null) {
			element.img = ImageIO.read(in);
		}
	}

	static void injectReceivedPhoto(Integer photoId, BufferedImage image) {
        CacheElement element = cache.getIfPresent(photoId);
        if (element != null) {
            element.img = image;
        }
    }
	
	public static class CacheElement {
		
		private static final ResourceLocation DUMMY = new ResourceLocation("cameracraft", "textures/gui/loadingPhoto.png");
		
		BufferedImage img;
		ResourceLocation loc;
		DynamicTexture tex;
		
		public void bindTexture() {
			TextureManager engine = mc.renderEngine;
			if (loc == null) {
				if (img == null) {
					engine.bindTexture(DUMMY);
					return;
				} else { 
					tex = new DynamicTexture(img);
					loc = engine.getDynamicTextureLocation("cameracraft.photo", tex);
				}
			}
			engine.bindTexture(loc);
		}

		public boolean isLoaded() {
			return img != null;
		}
	}
	
	static class PhotoDataLoader extends CacheLoader<Long, CacheElement> {

		@Override
		public CacheElement load(Long photoId) throws Exception {
			new PacketClientRequestPhoto(photoId).sendToServer();
			return new CacheElement();
		}
		
	}

}
