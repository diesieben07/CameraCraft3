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

import net.minecraft.entity.player.EntityPlayer;

import com.google.common.io.ByteStreams;

import cpw.mods.fml.relauncher.Side;
import de.take_weiland.mods.cameracraft.CCUtil;
import de.take_weiland.mods.commons.network.AbstractMultipartPacket;
import de.take_weiland.mods.commons.network.PacketType;

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
	public void read(EntityPlayer player, Side side, InputStream in) throws IOException {
		File file = CCUtil.getNextPhotoFile(player);
		
		@SuppressWarnings("resource") // damn you eclipse!
		FileChannel fileChannel = new FileOutputStream(file).getChannel();
		
		ByteStreams.copy(Channels.newChannel(in), fileChannel);
		
		fileChannel.close();
	}

	@Override
	public void execute(EntityPlayer player, Side side) {
//		new PacketClientAction(Action.NAME_PHOTO).sendTo(player);
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
