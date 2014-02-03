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

import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.cameracraft.network.PacketClientRequestPhoto;
import de.take_weiland.mods.commons.client.Rendering;

public class PhotoDataCache {

	static final Minecraft mc = Minecraft.getMinecraft();
	private static final LoadingCache<String, CacheElement> cache;
	
	static {
		cache = CacheBuilder.newBuilder()
				.concurrencyLevel(CameraCraft.maxThreadCount + 1)
				.expireAfterAccess(3, TimeUnit.MINUTES) // TODO figure out the right value here
				.removalListener(new PhotoTextureUnloader())
				.build(new PhotoDataLoader());
	}
	
	private PhotoDataCache() { }
	
	static void invalidate() {
		cache.invalidateAll();
	}
	
	public static void bindTexture(String photoId) {
		cache.getUnchecked(photoId).bindTexture();
	}
	
	static void injectReceivedPhoto(String photoId, InputStream in) throws IOException {
		CacheElement element = cache.getIfPresent(photoId);
		if (element != null) {
			System.out.println("injecting photo " + photoId);
			element.img = ImageIO.read(in);
			System.out.println("injected: " + element.img);
		}
	}
	
	public static class CacheElement {
		
		// TODO something else!
		private static final ResourceLocation DUMMY = new ResourceLocation("textures/painting/paintings_kristoffer_zetterstrand.png");
		
		BufferedImage img;
		ResourceLocation loc;
		DynamicTexture tex;
		
		void bindTexture() {
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
	}
	
	static class PhotoDataLoader extends CacheLoader<String, CacheElement> {

		@Override
		public CacheElement load(String photoId) throws Exception {
			new PacketClientRequestPhoto(photoId).sendToServer();
			return new CacheElement();
		}
		
	}
	
	static class PhotoTextureUnloader implements RemovalListener<String, CacheElement> {
		
		@Override
		public void onRemoval(RemovalNotification<String, CacheElement> notification) {
			ResourceLocation loc = notification.getValue().loc;
			if (loc != null) {
				Rendering.unloadTexture(loc);
			}
		}
		
	}

}
