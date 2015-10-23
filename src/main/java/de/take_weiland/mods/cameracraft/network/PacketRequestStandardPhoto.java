package de.take_weiland.mods.cameracraft.network;

import cpw.mods.fml.relauncher.Side;
import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.commons.net.MCDataInput;
import de.take_weiland.mods.commons.net.MCDataOutput;
import de.take_weiland.mods.commons.net.Packet;
import net.minecraft.entity.player.EntityPlayer;

import java.util.concurrent.CompletionStage;

@Packet.Receiver(Side.CLIENT)
public class PacketRequestStandardPhoto implements Packet.WithResponse<PacketTakenPhoto> {

	public PacketRequestStandardPhoto() {}

	public PacketRequestStandardPhoto(MCDataInput in) {}

	@Override
	public void writeTo(MCDataOutput out) {
	}

	public CompletionStage<PacketTakenPhoto> handle(EntityPlayer player) {
		return CameraCraft.proxy.handleStandardPhotoRequest();
	}
	
}
