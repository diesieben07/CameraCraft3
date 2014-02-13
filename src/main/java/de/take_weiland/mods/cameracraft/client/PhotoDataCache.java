package de.take_weiland.mods.cameracraft.client;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

import de.take_weiland.mods.cameracraft.network.PacketClientRequestPhoto;
import de.take_weiland.mods.commons.client.Rendering;

public class PhotoDataCache {

	static final Minecraft mc = Minecraft.getMinecraft();
	private static final LoadingCache<Integer, CacheElement> cache;
	
	static {
		cache = CacheBuilder.newBuilder()
				.concurrencyLevel(2) // everything higher is unlikely to ever happen
				.expireAfterAccess(3, TimeUnit.MINUTES) // TODO figure out the right value here
				.removalListener(new PhotoTextureUnloader())
				.build(new PhotoDataLoader());
	}
	
	private PhotoDataCache() { }
	
	static void invalidate() {
		cache.invalidateAll();
	}
	
	public static void bindTexture(Integer photoId) {
		cache.getUnchecked(photoId).bindTexture();
	}

	public static CacheElement get(Integer photoId) {
		return cache.getUnchecked(photoId);
	}
	
	static void injectReceivedPhoto(Integer photoId, InputStream in) throws IOException {
		CacheElement element = cache.getIfPresent(photoId);
		if (element != null) {
			element.img = ImageIO.read(in);
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
	
	static class PhotoDataLoader extends CacheLoader<Integer, CacheElement> {

		@Override
		public CacheElement load(Integer photoId) throws Exception {
			new PacketClientRequestPhoto(photoId).sendToServer();
			return new CacheElement();
		}
		
	}
	
	static class PhotoTextureUnloader implements RemovalListener<Integer, CacheElement> {
		
		@Override
		public void onRemoval(RemovalNotification<Integer, CacheElement> notification) {
			ResourceLocation loc = notification.getValue().loc;
			if (loc != null) {
				Rendering.unloadTexture(loc);
			}
		}
		
	}

}
