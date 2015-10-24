package de.take_weiland.mods.cameracraft.client;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import de.take_weiland.mods.cameracraft.network.PacketClientRequestPhoto;
import de.take_weiland.mods.cameracraft.network.PacketPhotoData;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@ParametersAreNonnullByDefault
public class PhotoDataCache {

	static final Minecraft mc = Minecraft.getMinecraft();
	private static final LoadingCache<Long, CacheElement> cache;
	
	static {
		cache = CacheBuilder.newBuilder()
				.concurrencyLevel(2) // everything higher is unlikely to ever happen
				.expireAfterAccess(3, TimeUnit.MINUTES) // TODO figure out the right value here
				.build(new PhotoDataLoader());
	}
	
	private PhotoDataCache() { }

	static void invalidate() {
		cache.invalidateAll();
	}
	
	public static ResourceLocation bindTexture(long photoId) {
		return cache.getUnchecked(photoId).bindTexture();
	}

    public static CompletableFuture<CacheElement> get(long photoId) {
        CacheElement element = cache.getIfPresent(photoId);
        if (element != null) {
            return CompletableFuture.completedFuture(element);
        } else {
            return CompletableFuture.supplyAsync(() -> cache.getUnchecked(photoId));
        }
    }
	
	static void injectReceivedPhoto(long photoId, InputStream in) throws IOException {
		CacheElement element = cache.getIfPresent(photoId);
		if (element != null) {
			element.img = ImageIO.read(in);
		}
	}

    public static class CacheElement implements Consumer<PacketPhotoData> {
		
		private static final ResourceLocation DUMMY = new ResourceLocation("cameracraft", "textures/gui/loadingPhoto.png");

        private BufferedImage img;
        private ResourceLocation loc;
        private DynamicTexture tex;
		
		public ResourceLocation bindTexture() {
			TextureManager engine = mc.renderEngine;
			if (loc == null) {
				if (img == null) {
					engine.bindTexture(DUMMY);
					return null;
				} else { 
					tex = new DynamicTexture(img);
					loc = engine.getDynamicTextureLocation("cameracraft.photo", tex);
				}
			}
			engine.bindTexture(loc);
            return loc;
		}

        @Override
        public void accept(PacketPhotoData packet) {
            img = packet.getImage();
        }

        public boolean isLoaded() {
			return img != null;
		}
	}
	
	static class PhotoDataLoader extends CacheLoader<Long, CacheElement> {

		@Override
		public CacheElement load(Long photoId) throws Exception {
			CacheElement element = new CacheElement();
            new PacketClientRequestPhoto(photoId).sendToServer()
                    .thenAcceptAsync(element, Scheduler.client());
			return element;
		}
		
	}

}
