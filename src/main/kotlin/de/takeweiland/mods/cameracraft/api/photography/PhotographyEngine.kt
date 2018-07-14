package de.takeweiland.mods.cameracraft.api.photography

import kotlinx.coroutines.experimental.Deferred
import net.minecraft.entity.player.EntityPlayerMP

/**
 * @author Take Weiland
 */
interface PhotographyEngine {

    fun takePhoto(player: EntityPlayerMP, size: PhotoSize): Deferred<Photo>

}