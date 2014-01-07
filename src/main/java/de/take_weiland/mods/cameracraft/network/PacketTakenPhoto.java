package de.take_weiland.mods.cameracraft.network;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.relauncher.Side;
import de.take_weiland.mods.cameracraft.PhotoRequestManager;
import de.take_weiland.mods.commons.network.MultipartDataPacket;
import de.take_weiland.mods.commons.network.PacketType;

public class PacketTakenPhoto extends MultipartDataPacket {

	private BufferedImage image;
	private int transferId;
	
	public PacketTakenPhoto(int transferId, BufferedImage image) {
		this.transferId = transferId;
		this.image = image;
	}
	
	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeInt(transferId);
		ImageIO.write(image, "PNG", out); // TODO: improve this?
	}
	
	@Override
	public int expectedSize() {
		return 50000; // roughly 50KB, maybe change later
	}

	@Override
	public void read(EntityPlayer player, Side side, final DataInputStream in) throws IOException {
		transferId = in.readInt();
		image = ImageIO.read(in);
		PhotoRequestManager.incomingPhoto(player, transferId, image);
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
