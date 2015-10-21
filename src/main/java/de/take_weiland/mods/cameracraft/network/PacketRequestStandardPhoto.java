package de.take_weiland.mods.cameracraft.network;

import cpw.mods.fml.relauncher.Side;
import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.commons.net.MCDataInput;
import de.take_weiland.mods.commons.net.MCDataOutput;
import de.take_weiland.mods.commons.net.Packet;
import net.minecraft.entity.player.EntityPlayer;

@Packet.Receiver(Side.CLIENT)
public class PacketRequestStandardPhoto implements Packet {

	private int transferId;
	
	public PacketRequestStandardPhoto(int transferId) {
		this.transferId = transferId;
	}

	public PacketRequestStandardPhoto(MCDataInput in) {
		this.transferId = in.readInt();
	}

	@Override
	public void writeTo(MCDataOutput out) {
		out.writeInt(transferId);
	}

	public void handle(EntityPlayer player) {
		CameraCraft.proxy.handleStandardPhotoRequest(transferId);
	}
	
}
