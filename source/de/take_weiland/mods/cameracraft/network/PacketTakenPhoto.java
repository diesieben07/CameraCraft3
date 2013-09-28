package de.take_weiland.mods.cameracraft.network;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;

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
			int idx = inv.getPhotoStorage().store(photoId);
			inv.closeChest();
			if (idx < 0) {
				warnHack(player);
			} else {
				CameraCraft.executor.execute(new ImageDataSaver(photoId, in, file));
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

		ImageDataSaver(String photoId, InputStream in, File file) {
			this.photoId = photoId;
			this.in = in;
			this.file = file;
		}

		@Override
		public void run() {
			try {
				OutputStream out = new FileOutputStream(file);
//				SimpleRgbFilter filter = new OverexposeFilter();
//				ImageIO.write(ImageFilters.apply(ImageIO.read(in), filter), "PNG", out);
//				out.close();
				
				@SuppressWarnings("resource") // damn you eclipse!
				FileChannel fileChannel = new FileOutputStream(file).getChannel();
			
				ByteStreams.copy(Channels.newChannel(in), fileChannel);
			
				fileChannel.close();
			} catch (final IOException e) {
				Scheduler.server().schedule(new Runnable() {
					@Override
					public void run() {
						CrashReport cr = CrashReport.makeCrashReport(e, "Saving CameraCraft Image file");
						cr.makeCategory("Photo being saved").addCrashSection("PhotoId", photoId);
						throw new ReportedException(cr);
					}
				});
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
