package de.takeweiland.mods.cameracraft.api.photography

import net.minecraft.entity.player.EntityPlayerMP
import java.awt.image.BufferedImage

/**
 * @author Take Weiland
 */
data class Photo(val player: EntityPlayerMP, val image: BufferedImage)