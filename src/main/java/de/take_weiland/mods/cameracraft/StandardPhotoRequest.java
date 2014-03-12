package de.take_weiland.mods.cameracraft;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import de.take_weiland.mods.cameracraft.network.PacketRequestStandardPhoto;
import net.minecraft.entity.player.EntityPlayer;

import java.awt.image.BufferedImage;

/**
 * @author diesieben07
 */
public class StandardPhotoRequest implements PhotoRequest {

	private final int transferId;
	private final SettableFuture<BufferedImage> future = SettableFuture.create();

	public StandardPhotoRequest(int transferId) {
		this.transferId = transferId;
	}

	@Override
	public void setImage(BufferedImage image) {
		future.set(image);
	}

	@Override
	public ListenableFuture<BufferedImage> getFuture() {
		return future;
	}

	@Override
	public void send(EntityPlayer player) {
		new PacketRequestStandardPhoto(transferId).sendTo(player);
	}
}
