package de.take_weiland.mods.cameracraft.network;

import cpw.mods.fml.relauncher.Side;
import de.take_weiland.mods.cameracraft.photo.DatabaseImpl;
import de.take_weiland.mods.commons.net.MCDataInput;
import de.take_weiland.mods.commons.net.MCDataOutput;
import de.take_weiland.mods.commons.net.Packet;
import net.minecraft.entity.player.EntityPlayer;

import java.io.File;

@Packet.Receiver(Side.SERVER)
public class PacketClientRequestPhoto implements Packet {

    long photoId;
	
	public PacketClientRequestPhoto(long photoId) {
		this.photoId = photoId;
	}

    public PacketClientRequestPhoto(MCDataInput in) {
        photoId = in.readLong();
    }

    @Override
    public void writeTo(MCDataOutput out) {
        out.writeLong(photoId);
    }

    public void handle(EntityPlayer player) {
        long photoId = this.photoId;

        File file = DatabaseImpl.instance.getImageFile(photoId);
        if (file.exists()) {
            new PacketPhotoData(photoId, file).sendTo(player);
        }
    }

}
