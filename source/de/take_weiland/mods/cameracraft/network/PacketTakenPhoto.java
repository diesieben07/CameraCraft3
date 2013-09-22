package de.take_weiland.mods.cameracraft.network;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;

import javax.imageio.ImageIO;

import net.minecraft.entity.player.EntityPlayer;

import com.google.common.io.ByteStreams;

import cpw.mods.fml.relauncher.Side;
import de.take_weiland.mods.cameracraft.CCUtil;
import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.commons.network.MultipartDataPacket;
import de.take_weiland.mods.commons.network.PacketType;

public class PacketTakenPhoto extends MultipartDataPacket {

	private BufferedImage image;
	private String name;
	
	public PacketTakenPhoto(BufferedImage image, String name) {
		this.image = image;
		this.name = name;
	}
	
	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeUTF(name);
		ImageIO.write(image, "PNG", out); // TODO: improve this?
	}
	
	@Override
	public int expectedSize() {
		return 50000; // roughly 50KB, maybe change later
	}

	@Override
	public void read(EntityPlayer player, DataInputStream in) throws IOException {
		name = in.readUTF();
		File file = CCUtil.getNextPhotoFile(player);
		
		@SuppressWarnings("resource") // damn you eclipse!
		FileChannel fileChannel = new FileOutputStream(file).getChannel();
		
		ByteStreams.copy(Channels.newChannel(in), fileChannel);
		
		fileChannel.close();
	}

	@Override
	public void execute(EntityPlayer player, Side side) {
		switch (side) {
		case CLIENT:
			CameraCraft.env.handleClientPhotoData(image);
			break;
		case SERVER:
			
			break;
		}
	}
	
	@Override
	public PacketType type() {
		return CCPackets.TAKEN_PHOTO;
	}
	
	@Override
	public boolean isValidForSide(Side side) {
		return side.isServer();
	}
}
