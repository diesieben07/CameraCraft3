package de.take_weiland.mods.cameracraft.network;

import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.relauncher.Side;
import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.commons.network.AbstractModPacket;
import de.take_weiland.mods.commons.network.PacketType;

public class PacketTakePhoto extends AbstractModPacket {

	@Override
	public boolean isValidForSide(Side side) {
		return side.isClient();
	}

	@Override
	public void execute(EntityPlayer player, Side side) {
		CameraCraft.env.executePhoto();
	}

	@Override
	public byte[] getData(int spareBytes) {
		return new byte[spareBytes];
	}

	@Override
	public void handleData(byte[] data, int offset) { }

	@Override
	public PacketType type() {
		return CCPackets.TAKE_PHOTO;
	}

}
