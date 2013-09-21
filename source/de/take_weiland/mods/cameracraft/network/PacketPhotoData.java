package de.take_weiland.mods.cameracraft.network;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.relauncher.Side;
import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.commons.network.AbstractMultipartPacket;

public class PacketPhotoData extends AbstractMultipartPacket {

	private BufferedImage image;
	
	public PacketPhotoData(BufferedImage image) {
		this.image = image;
	}
	
	@Override
	public MultipartPacketType type() {
		return CCPackets.MultiPackets.PHOTODATA;
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
	public void read(EntityPlayer player, InputStream in) throws IOException {
		image = ImageIO.read(in);
	}

	@Override
	public boolean isValidForSide(Side side) {
		return true;
	}

	@Override
	public void execute(EntityPlayer player, Side side) {
		switch (side) {
		case CLIENT:
			CameraCraft.env.handleClientPhotoData(image);
			break;
		case SERVER:
			try {
				ImageIO.write(image, "PNG", new File("C:/Users/take/desktop/test.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
	}
	
}
