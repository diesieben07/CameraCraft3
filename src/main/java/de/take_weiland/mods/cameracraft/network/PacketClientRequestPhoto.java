package de.take_weiland.mods.cameracraft.network;

import java.io.File;

import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.relauncher.Side;
import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.cameracraft.photo.PhotoManager;
import de.take_weiland.mods.commons.net.DataBuf;
import de.take_weiland.mods.commons.net.WritableDataBuf;

public class PacketClientRequestPhoto extends CCPacket {

	int photoId;
	
	public PacketClientRequestPhoto(String photoId) {
		this.photoId = PhotoManager.asInt(photoId);
	}

	@Override
	protected void write(WritableDataBuf buffer) {
		buffer.putVarInt(photoId);
	}

	@Override
	protected void handle(DataBuf buffer, final EntityPlayer player, Side side) {
		photoId = buffer.getVarInt();
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
	protected boolean validOn(Side side) {
		return side.isServer();
	}

}
