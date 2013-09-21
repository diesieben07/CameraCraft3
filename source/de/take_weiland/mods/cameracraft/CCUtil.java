package de.take_weiland.mods.cameracraft;

import java.io.File;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.DimensionManager;

public final class CCUtil {

	private CCUtil() { }
	
	public static File getNextPhotoFile(EntityPlayer player) {
		CCPlayerData data = CCPlayerData.get(player);
		File root = new File(DimensionManager.getCurrentSaveRootDirectory(), "cameracraft.photos");
		root.mkdirs();
		return new File(root, player.username.toLowerCase() + "_" + data.nextId() + ".png");
	}
	
}
