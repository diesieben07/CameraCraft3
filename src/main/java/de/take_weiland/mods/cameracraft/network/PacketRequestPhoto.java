package de.take_weiland.mods.cameracraft.network;

import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.relauncher.Side;
import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.commons.net.DataBuf;
import de.take_weiland.mods.commons.net.WritableDataBuf;

public class PacketRequestPhoto extends CCPacket {

	private int transferId;
	
	public PacketRequestPhoto(int transferId) {
		this.transferId = transferId;
	}
	
	@Override
	protected void write(WritableDataBuf buffer) {
		buffer.putVarInt(transferId);
	}

	@Override
	protected void handle(DataBuf buffer, EntityPlayer player, Side side) {
		transferId = buffer.getVarInt();
		CameraCraft.env.onPhotoRequest(transferId);
	}
	
	@Override
	protected boolean validOn(Side side) {
		return side.isClient();
	}

}
