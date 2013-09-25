package de.take_weiland.mods.cameracraft;

import java.io.File;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.DimensionManager;

public final class CCUtil {

	public static final int PHOTO_SIZE = 256;

	private CCUtil() { }
	
	public static File getNextPhotoFile(EntityPlayer player) {
		CCPlayerData data = CCPlayerData.get(player);
		File root = makeRoot();
		return new File(root, player.username.toLowerCase() + "_" + data.nextId() + ".png");
	}
	
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
