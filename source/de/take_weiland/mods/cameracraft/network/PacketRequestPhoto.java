package de.take_weiland.mods.cameracraft.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.relauncher.Side;
import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.commons.network.DataPacket;
import de.take_weiland.mods.commons.network.PacketType;

public class PacketRequestPhoto extends DataPacket {

	private int transferId;
	
	public PacketRequestPhoto(int transferId) {
		this.transferId = transferId;
	}
	
	@Override
	protected void write(DataOutputStream out) throws IOException {
		out.writeInt(transferId);
	}

	@Override
	protected void read(EntityPlayer player, Side side, DataInputStream in) throws IOException {
		transferId = in.readInt();
	}

	@Override
	public void execute(EntityPlayer player, Side side) {
		CameraCraft.env.onPhotoRequest(transferId);
	}
	
	@Override
	public boolean isValidForSide(Side side) {
		return side.isClient();
	}
	
	@Override
	public PacketType type() {
		return CCPackets.REQUEST_PHOTO;
	}

}
