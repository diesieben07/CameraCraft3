package de.take_weiland.mods.cameracraft.network;

import cpw.mods.fml.relauncher.Side;
import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.commons.net.MCDataInput;
import de.take_weiland.mods.commons.net.MCDataOutput;
import de.take_weiland.mods.commons.net.Packet;
import net.minecraft.entity.player.EntityPlayer;

import java.util.concurrent.CompletionStage;

@Packet.Receiver(Side.SERVER)
public class PacketClientRequestPhoto implements Packet.WithResponse<PacketPhotoData> {

    private final long photoId;
	
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

    public CompletionStage<PacketPhotoData> handle(EntityPlayer player) {
        return CameraCraft.currentDatabase().getImageAsync(photoId)
                .thenApply(PacketPhotoData::new);
    }

}
