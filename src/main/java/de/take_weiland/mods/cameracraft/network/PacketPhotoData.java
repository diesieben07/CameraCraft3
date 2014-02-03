package de.take_weiland.mods.cameracraft.network;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import net.minecraft.crash.CrashReport;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ReportedException;

import com.google.common.io.ByteStreams;

import cpw.mods.fml.relauncher.Side;
import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.cameracraft.photo.PhotoManager;
import de.take_weiland.mods.commons.net.DataBuf;
import de.take_weiland.mods.commons.net.WritableDataBuf;

public class PacketPhotoData extends CCPacket {

	private int photoId;
	private File file;

	public PacketPhotoData(int photoId, File file) {
		this.photoId = photoId;
		this.file = file;
	}

	@Override
	protected void write(WritableDataBuf buffer) {
		buffer.putVarInt(photoId);
		try {
			FileInputStream in = new FileInputStream(file);
			ByteStreams.copy(in, buffer.asOutputStream());
			in.close();
		} catch (IOException e) {
			CrashReport cr = new CrashReport("Reading CameraCraft Photo File", e);
			cr.makeCategory("Photo being read").addCrashSection("PhotoId", Integer.valueOf(photoId));
			throw new ReportedException(cr);
		}
	}

	@Override
	protected void handle(DataBuf buffer, EntityPlayer player, Side side) {
		CameraCraft.env.handleClientPhotoData(PhotoManager.asString(buffer.getVarInt()), buffer.asInputStream());
	}
	
	@Override
	protected boolean validOn(Side side) {
		return side.isClient();
	}

}
