package de.take_weiland.mods.cameracraft.photo;

import net.minecraft.crash.CrashReport;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ReportedException;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;
import de.take_weiland.mods.cameracraft.CCUtil;

public class PhotoDataManager extends WorldSavedData {

	private static final String IDENTIFIER = "cameracraft.datamanager";

	public PhotoDataManager() {
		super(IDENTIFIER);
	}
	
	private PhotoData getData(String photoId) {
		try {
			return PhotoData.fromFile(photoId, CCUtil.getDataFile(photoId));
		} catch (Exception e) {
			CrashReport cr = CrashReport.makeCrashReport(e, "Loading CameraCraft PhotoData");
			cr.makeCategory("PhotoData being loaded").addCrashSection("PhotoId", photoId);
			throw new ReportedException(cr);
		}
	}
	
	private static PhotoDataManager get() {
		MapStorage ms = MinecraftServer.getServer().worldServers[0].mapStorage;
		PhotoDataManager manager = (PhotoDataManager) ms.loadData(PhotoDataManager.class, IDENTIFIER);
		if (manager == null) {
			ms.setData(IDENTIFIER, (manager = new PhotoDataManager()));
		}
		return manager;
	}
	
	public static PhotoData getDataForId(String photoId) {
		return get().getData(photoId);
	}
	
	// we never save anything, this should never be called
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		throw new AssertionError("PhotoDataManager should never be loaded!");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		throw new AssertionError("PhotoDataManager should never be saved!");
	}

}
