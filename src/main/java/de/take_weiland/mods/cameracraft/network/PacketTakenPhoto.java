package de.take_weiland.mods.cameracraft.network;

import cpw.mods.fml.relauncher.Side;
import de.take_weiland.mods.cameracraft.CCPlayerData;
import de.take_weiland.mods.commons.net.MCDataInput;
import de.take_weiland.mods.commons.net.MCDataOutput;
import de.take_weiland.mods.commons.net.Packet;
import de.take_weiland.mods.commons.net.ProtocolException;
import net.minecraft.entity.player.EntityPlayer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Packet.Receiver(Side.SERVER)
public class PacketTakenPhoto implements Packet {

	private BufferedImage image;
	private int transferId;
	
	public PacketTakenPhoto(int transferId, BufferedImage image) {
		this.transferId = transferId;
		this.image = image;
	}

	public PacketTakenPhoto(MCDataInput in) {
		this.transferId = in.readInt();
		try {
			this.image = ImageIO.read(in.asInputStream());
		} catch (IOException e) {
			throw new ProtocolException("Invalid Image");
		}
	}
	
	@Override
	public void writeTo(MCDataOutput out) {
		out.writeInt(transferId);
		try {
			ImageIO.write(image, "PNG", out.asOutputStream()); // TODO: improve this?
		} catch (IOException e) {
			throw new AssertionError("Impossible");
		}
	}
	
	public void handle(EntityPlayer player) {
		CCPlayerData.get(player).onPhoto(transferId, image);
	}
	
	@Override
	public int expectedSize() {
		return 50000; // roughly 50KB, maybe change later
	}

}