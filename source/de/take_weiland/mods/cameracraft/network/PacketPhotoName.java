package de.take_weiland.mods.cameracraft.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.relauncher.Side;
import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.commons.network.DataPacket;
import de.take_weiland.mods.commons.network.PacketType;

public class PacketPhotoName extends DataPacket {

	private String previousName;
	
	public PacketPhotoName(String previousName) {
		this.previousName = previousName;
	}
	
	@Override
	public int expectedSize() {
		int len = previousName.length();
		return 2 + len + len >> 1;
	}

	@Override
	protected void write(DataOutputStream out) throws IOException {
		out.writeUTF(previousName);
	}

	@Override
	protected void read(EntityPlayer player, Side side, DataInputStream in) throws IOException {
		previousName = in.readUTF();
	}
	
	@Override
	public void execute(EntityPlayer player, Side side) {
		CameraCraft.env.displayNamePhotoGui(previousName);
	}

	@Override
	public boolean isValidForSide(Side side) {
		return side.isClient();
	}

	@Override
	public PacketType type() {
		return CCPackets.PHOTO_NAME;
	}
	
}
