package de.take_weiland.mods.cameracraft.network;

import de.take_weiland.mods.commons.net.ModPacket;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.relauncher.Side;
import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.commons.net.DataBuf;
import de.take_weiland.mods.commons.net.WritableDataBuf;

public class PacketRequestStandardPhoto extends ModPacket {

	private int transferId;
	
	public PacketRequestStandardPhoto(int transferId) {
		this.transferId = transferId;
	}
	
	@Override
	protected void write(WritableDataBuf buffer) {
		buffer.putVarInt(transferId);
	}

	@Override
	protected void handle(DataBuf buffer, EntityPlayer player, Side side) {
		transferId = buffer.getVarInt();
		CameraCraft.env.handleStandardPhotoRequest(transferId);
	}
	
	@Override
	protected boolean validOn(Side side) {
		return side.isClient();
	}

}
