package de.take_weiland.mods.cameracraft;

import java.io.File;

import net.minecraftforge.common.DimensionManager;

public final class CCUtil {

	public static final int PHOTO_SIZE = 256;

	private CCUtil() { }
	
	private static File makeRoot() {
		File root = new File(DimensionManager.getCurrentSaveRootDirectory(), "cameracraft.photos");
		root.mkdirs();
		return root;
	}
	
	public static File getImageFile(String photoId) {
		File root = makeRoot();
		return new File(root, photoId + ".png");
	}

	public static File getDataFile(String photoId) {
		return new File(makeRoot(), photoId + ".dat");
	}
	
}
