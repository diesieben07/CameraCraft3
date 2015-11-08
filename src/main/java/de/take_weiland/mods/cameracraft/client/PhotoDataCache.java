package de.take_weiland.mods.cameracraft.client;

import com.google.common.cache.*;
import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.cameracraft.network.PacketClientRequestPhoto;
import de.take_weiland.mods.cameracraft.network.PacketPhotoData;
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
import java.util.function.Consumer;

/**
 * <p>Client side cache for photo textures.</p>
 */
@ParametersAreNonnullByDefault
public class PhotoDataCache {

	static final Minecraft mc = Minecraft.getMinecraft();
	private static final LoadingCache<Long, CacheElement> cache;

	static {
        CacheManager manager = new CacheManager();
        cache = CacheBuilder.newBuilder()
				.concurrencyLevel(2) // everything higher is unlikely to ever happen
				.expireAfterAccess(3, TimeUnit.MINUTES) // TODO figure out the right value here
                .removalListener(manager)
				.build(manager);
	}

	private PhotoDataCache() { }

    /**
     * <p>Invalidate all cached photos</p>
     */
	public static void invalidate() {
		cache.invalidateAll();
	}

	public static ResourceLocation bindTexture(long photoId) {
		return cache.getUnchecked(photoId).bindTexture();
	}

    static void injectReceivedPhoto(long photoId, InputStream in) throws IOException {
		CacheElement element = cache.getIfPresent(photoId);
		if (element != null) {
			element.img = ImageIO.read(in);
		}
	}

    public static CacheElement get(long photoID) {
        return cache.getUnchecked(photoID);
    }

    public static class CacheElement implements Consumer<PacketPhotoData> {

		private static final ResourceLocation DUMMY = new ResourceLocation("cameracraft", "textures/gui/loadingPhoto.png");

        private BufferedImage    img;
        private ResourceLocation loc;
        private DynamicTexture   tex;

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

		public DynamicTexture getTexture() {
            return tex;
        }

		public BufferedImage getImage() {
            return img;
        }

        void unload() {
            if (loc != null) {
                Rendering.unloadTexture(loc);
                loc = null;
            }
        }
	}

	static class CacheManager extends CacheLoader<Long, CacheElement> implements RemovalListener<Long, CacheElement> {

		@Override
		public CacheElement load(Long photoId) throws Exception {
			CacheElement element = new CacheElement();
            new PacketClientRequestPhoto(photoId).sendToServer()
                    .thenAcceptAsync(element, Scheduler.client());
			return element;
		}

        @Override
        public void onRemoval(RemovalNotification<Long, CacheElement> notification) {
            CacheElement element = notification.getValue();
            if (element != null) {
                Scheduler.client().execute(element::unload);
            } else {
                CameraCraft.logger.warn("Tried to unload null client overDynText cache");
            }
        }
    }

}
