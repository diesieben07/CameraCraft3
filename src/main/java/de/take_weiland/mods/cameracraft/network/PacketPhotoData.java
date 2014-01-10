package de.take_weiland.mods.cameracraft.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;

import net.minecraft.entity.player.EntityPlayer;

import com.google.common.io.ByteStreams;

import cpw.mods.fml.relauncher.Side;
import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.commons.network.MultipartDataPacket;
import de.take_weiland.mods.commons.network.PacketType;

public class PacketPhotoData extends MultipartDataPacket {

	private String photoId;
	private File file;

	public PacketPhotoData(String photoId, File file) {
		this.photoId = photoId;
		this.file = file;
	}

	@Override
	protected void write(DataOutputStream out) throws IOException {
		out.writeUTF(photoId);
		ReadableByteChannel in = Files.newByteChannel(file.toPath());
		ByteStreams.copy(in, Channels.newChannel(out));
		in.close();
	}

	@Override
	protected void read(EntityPlayer player, Side side, DataInputStream in) throws IOException {
		photoId = in.readUTF();
		CameraCraft.env.handleClientPhotoData(photoId, in);
	}
	

	@Override
	public void execute(EntityPlayer player, Side side) { }
	
	@Override
	public boolean isValidForSide(Side side) {
		return side.isClient();
	}

	@Override
	public PacketType type() {
		return CCPackets.PHOTO_DATA;
	}

}
