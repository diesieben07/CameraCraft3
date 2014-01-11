package de.take_weiland.mods.cameracraft.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.relauncher.Side;
import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.cameracraft.photo.PhotoManager;
import de.take_weiland.mods.commons.network.DataPacket;
import de.take_weiland.mods.commons.network.PacketType;

public class PacketClientRequestPhoto extends DataPacket {

	int photoId;
	
	public PacketClientRequestPhoto(String photoId) {
		this.photoId = PhotoManager.asInt(photoId);
	}

	@Override
	protected void write(DataOutputStream out) throws IOException {
		out.writeInt(photoId);
	}

	@Override
	protected void read(EntityPlayer player, Side side, DataInputStream in) throws IOException {
		photoId = in.readInt();
	}
	
	@Override
	public void execute(final EntityPlayer player, Side side) {
		CameraCraft.executor.execute(new Runnable() {
			
			@Override
			public void run() {
				File file = PhotoManager.getImageFile(photoId);
				if (file.exists()) {
					new PacketPhotoData(photoId, file).sendTo(player);
				}
			}
		});
	}

	@Override
	public boolean isValidForSide(Side side) {
		return side.isServer();
	}
	
	@Override
	public PacketType type() {
		return CCPackets.CLIENT_REQUEST_PHOTO;
	}

}
