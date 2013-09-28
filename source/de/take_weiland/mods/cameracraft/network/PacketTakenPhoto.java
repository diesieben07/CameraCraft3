package de.take_weiland.mods.cameracraft.network;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import net.minecraft.crash.CrashReport;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ReportedException;

import com.google.common.io.ByteStreams;

import cpw.mods.fml.relauncher.Side;
import de.take_weiland.mods.cameracraft.CCWorldData;
import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.cameracraft.api.img.ImageFilter;
import de.take_weiland.mods.cameracraft.api.photo.PhotoStorage;
import de.take_weiland.mods.cameracraft.img.ImageFilters;
import de.take_weiland.mods.cameracraft.inv.InventoryCamera;
import de.take_weiland.mods.cameracraft.item.CCItem;
import de.take_weiland.mods.cameracraft.photo.PhotoManager;
import de.take_weiland.mods.commons.network.AbstractMultipartPacket;
import de.take_weiland.mods.commons.network.PacketType;
import de.take_weiland.mods.commons.util.Scheduler;

public class PacketTakenPhoto extends AbstractMultipartPacket {

	private BufferedImage image;
	
	public PacketTakenPhoto(BufferedImage image) {
		this.image = image;
	}
	
	@Override
	public void write(OutputStream out) throws IOException {
		ImageIO.write(image, "PNG", out); // TODO: improve this?
	}
	
	@Override
	public int expectedSize() {
		return 50000; // roughly 50KB, maybe change later
	}

	@Override
	public void read(EntityPlayer player, Side side, final InputStream in) throws IOException {
		final String photoId = CCWorldData.get(player.worldObj).nextId();
		final File file = PhotoManager.getImageFile(photoId);
		
		InventoryCamera inv = CCItem.camera.newInventory(player);
		if (inv.hasStorageItem()) {
			PhotoStorage storage = inv.getPhotoStorage();
			int idx = storage.store(photoId);
			
			ImageFilter combinedFilter = ImageFilters.combineNullable(inv.getLensFilter(), storage.getFilter());
			
			inv.closeChest();
			if (idx < 0) {
				warnHack(player);
			} else {
				CameraCraft.executor.execute(new ImageDataSaver(photoId, in, file, combinedFilter));
			}
		} else {
			warnHack(player);
		}
	}

	private static void warnHack(EntityPlayer player) {
		player.sendChatToPlayer(ChatMessageComponent.createFromText("Hey you! Don't hack!").setColor(EnumChatFormatting.DARK_RED));
	}
	
	private static final class ImageDataSaver implements Runnable {
		final String photoId;
		private final InputStream in;
		private final File file;
		private final ImageFilter filter;

		ImageDataSaver(String photoId, InputStream in, File file, ImageFilter filter) {
			this.photoId = photoId;
			this.in = in;
			this.file = file;
			this.filter = filter;
		}

		@Override
		public void run() {
			try {
				if (filter != null) {
					ImageIO.write(filter.apply(ImageIO.read(in)), "PNG", file);
				} else {
					OutputStream out = new FileOutputStream(file);
					ByteStreams.copy(in, out);
					out.close();
				}
			} catch (IOException e) {
				CrashReport cr = CrashReport.makeCrashReport(e, "Saving CameraCraft Image file");
				cr.makeCategory("Photo being saved").addCrashSection("PhotoId", photoId);
				Scheduler.server().throwInMainThread(new ReportedException(cr));
			}
		}
	}

	@Override
	public void execute(EntityPlayer player, Side side) { }
	
	@Override
	public PacketType type() {
		return CCPackets.TAKEN_PHOTO;
	}
	
	@Override
	public boolean isValidForSide(Side side) {
		return side.isServer();
	}
}
