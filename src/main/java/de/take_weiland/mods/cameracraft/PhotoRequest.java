package de.take_weiland.mods.cameracraft;

import com.google.common.util.concurrent.ListenableFuture;
import net.minecraft.entity.player.EntityPlayer;

import java.awt.image.BufferedImage;

/**
 * @author diesieben07
 */
public interface PhotoRequest {

	void setImage(BufferedImage image);

	void send(EntityPlayer player);

	ListenableFuture<BufferedImage> getFuture();

}
