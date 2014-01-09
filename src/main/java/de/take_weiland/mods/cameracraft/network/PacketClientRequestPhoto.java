package de.take_weiland.mods.cameracraft.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.relauncher.Side;
import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.commons.network.DataPacket;
import de.take_weiland.mods.commons.network.PacketType;

public class PacketClientRequestPhoto extends DataPacket {

	private String photoId;
	
	public PacketClientRequestPhoto(String photoId) {
		this.photoId = photoId;
	}

	@Override
	protected void write(DataOutputStream out) throws IOException {
		out.writeUTF(photoId);
	}

	@Override
	protected void read(EntityPlayer player, Side side, DataInputStream in) throws IOException {
		photoId = in.readUTF();
		CameraCraft.env.handleClientPhotoData(photoId, in);
	}

	@Override
	public boolean isValidForSide(Side side) {
		return side.isServer();
	}
	
	@Override
	public void execute(EntityPlayer player, Side side) { }

	@Override
	public PacketType type() {
		return CCPackets.CLIENT_REQUEST_PHOTO;
	}

}
