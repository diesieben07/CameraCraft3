package de.take_weiland.mods.cameracraft.photo;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

import javax.imageio.ImageIO;

import net.minecraft.crash.CrashReport;
import net.minecraft.util.ReportedException;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import com.google.common.collect.ImmutableList;
import com.google.common.io.Files;
import com.google.common.util.concurrent.ListenableFuture;

import de.take_weiland.mods.cameracraft.CCWorldData;
import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.cameracraft.api.img.ImageFilter;

public final class PhotoManager {

	private PhotoManager() { }
	
	public static final int PHOTO_SIZE = 256;
	
	public static String nextPhotoId(World world) {
		return CCWorldData.get(world).nextId();
	}
	
	public static File getImageFile(String photoId) {
		return getFile(photoId, "png");
	}

	public static File getDataFile(String photoId) {
		return getFile(photoId, "dat");
	}
	
	private static File getFile(String photoId, String ext) {
		File file = new File(DimensionManager.getCurrentSaveRootDirectory(), "cameracraft.photos/" + photoId + "." + ext);
		try {
			Files.createParentDirs(file);
		} catch (IOException e) {
			CrashReport cr = CrashReport.makeCrashReport(e, "Failed to create root dir for CameraCraft photos!");
			throw new ReportedException(cr);
		}
		return file;
	}

	public static PhotoData getDataForId(String id) {
		try {
			return PhotoData.fromFile(id, getDataFile(id));
		} catch (IOException e) {
			CrashReport cr = CrashReport.makeCrashReport(e, "Loading CameraCraft PhotoData");
			cr.makeCategory("Data being loaded").addCrashSection("PhotoId", id);
			throw new ReportedException(cr);
		}
	}
	
	public static List<ListenableFuture<Void>> applyFilterTo(final Iterable<String> photoIds, final ImageFilter filter) {
		ImmutableList.Builder<ListenableFuture<Void>> builder = ImmutableList.builder();
		
		for (final String photoId : photoIds) {
			builder.add(CameraCraft.executor.submit(new Callable<Void>() {
				
				@Override
				public Void call() throws IOException {
					File file = getImageFile(photoId);
					if (file.exists()) {
						ImageIO.write(filter.apply(ImageIO.read(file)), "PNG", file);
					}
					return null;
				}
			}));
		}
		return builder.build();
	}
	
}
