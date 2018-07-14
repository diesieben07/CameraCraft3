package de.takeweiland.mods.cameracraft.photography

import de.takeweiland.mods.cameracraft.api.photography.Photo
import de.takeweiland.mods.cameracraft.api.photography.PhotoSize
import de.takeweiland.mods.cameracraft.api.photography.PhotographyEngine
import kotlinx.coroutines.experimental.Deferred
import net.minecraft.entity.player.EntityPlayerMP

/**
 * @author Take Weiland
 */
object DefaultPhotographyEngine : PhotographyEngine {

    override fun takePhoto(player: EntityPlayerMP, size: PhotoSize): Deferred<Photo> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}