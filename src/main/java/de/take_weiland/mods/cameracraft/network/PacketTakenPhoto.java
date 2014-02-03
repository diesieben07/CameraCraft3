package de.take_weiland.mods.cameracraft.network;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.relauncher.Side;
import de.take_weiland.mods.cameracraft.PhotoRequestManager;
import de.take_weiland.mods.commons.net.DataBuf;
import de.take_weiland.mods.commons.net.WritableDataBuf;

public class PacketTakenPhoto extends CCPacket {

	private BufferedImage image;
	private int transferId;
	
	public PacketTakenPhoto(int transferId, BufferedImage image) {
		this.transferId = transferId;
		this.image = image;
	}
	
	@Override
	public void write(WritableDataBuf buf) {
		buf.putVarInt(transferId);
		try {
			ImageIO.write(image, "PNG", buf.asOutputStream()); // TODO: improve this?
		} catch (IOException e) {
			throw new AssertionError("Impossible");
		}
	}
	
	@Override
	protected void handle(DataBuf buf, EntityPlayer player, Side side) {
		try {
			PhotoRequestManager.incomingPhoto(player, buf.getVarInt(), ImageIO.read(buf.asInputStream()));
		} catch (IOException e) {
			throw new AssertionError("Impossible!");
		}
	}
	
	
	@Override
	protected boolean validOn(Side side) {
		return side.isServer();
	}


	@Override
	public int expectedSize() {
		return 50000; // roughly 50KB, maybe change later
	}

}