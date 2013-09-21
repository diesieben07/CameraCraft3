package de.take_weiland.mods.cameracraft.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.relauncher.Side;
import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.commons.network.AbstractPacket;
import de.take_weiland.mods.commons.network.PacketType;

public class PacketTakePhoto extends AbstractPacket {

	@Override
	public boolean isValidForSide(Side side) {
		return side.isClient();
	}

	@Override
	public void execute(EntityPlayer player, Side side) {
		CameraCraft.env.executePhoto();
	}

	@Override
	public PacketType type() {
		return CCPackets.TAKE_PHOTO;
	}

	@Override
	public void read(EntityPlayer player, InputStream in) throws IOException { }

	@Override
	public void write(OutputStream out) throws IOException { }

}
