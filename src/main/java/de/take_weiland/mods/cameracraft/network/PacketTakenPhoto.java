package de.take_weiland.mods.cameracraft.network;

import cpw.mods.fml.relauncher.Side;
import de.take_weiland.mods.commons.net.MCDataInput;
import de.take_weiland.mods.commons.net.MCDataOutput;
import de.take_weiland.mods.commons.net.Packet;
import de.take_weiland.mods.commons.net.ProtocolException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Packet.Receiver(Side.SERVER)
public class PacketTakenPhoto implements Packet.Response {

	public final BufferedImage image;

	public PacketTakenPhoto(BufferedImage image) {
		this.image = image;
	}

	public PacketTakenPhoto(MCDataInput in) {
		try {
			this.image = ImageIO.read(in.asInputStream());
		} catch (IOException e) {
			throw new ProtocolException("Invalid Image");
		}
	}
	
	@Override
	public void writeTo(MCDataOutput out) {
		try {
			ImageIO.write(image, "PNG", out.asOutputStream()); // TODO: improve this?
		} catch (IOException e) {
			throw new AssertionError("Impossible");
		}
	}
	
	@Override
	public int expectedSize() {
		return 50000; // roughly 50KB, maybe change later
	}

}