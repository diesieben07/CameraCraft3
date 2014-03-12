package de.take_weiland.mods.cameracraft.network;

import cpw.mods.fml.relauncher.Side;
import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.cameracraft.photo.PhotoManager;
import de.take_weiland.mods.commons.net.DataBuf;
import de.take_weiland.mods.commons.net.ModPacket;
import de.take_weiland.mods.commons.net.WritableDataBuf;
import net.minecraft.entity.player.EntityPlayer;

import java.io.File;

public class PacketClientRequestPhoto extends ModPacket {

	Integer photoId;
	
	public PacketClientRequestPhoto(Integer photoId) {
		this.photoId = photoId;
	}

	@Override
	protected void write(WritableDataBuf buffer) {
		buffer.putVarInt(photoId.intValue());
	}

	@Override
	protected void handle(DataBuf buffer, final EntityPlayer player, Side side) {
		photoId = Integer.valueOf(buffer.getVarInt());
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
