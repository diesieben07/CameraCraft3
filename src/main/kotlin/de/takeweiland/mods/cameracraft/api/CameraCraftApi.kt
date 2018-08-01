package de.takeweiland.mods.cameracraft.api

import de.takeweiland.mods.cameracraft.api.io.ImageIO
import de.takeweiland.mods.cameracraft.api.photography.PhotographyEngine
import de.takeweiland.mods.cameracraft.api.store.PhotoStore
import net.minecraft.world.World
import net.minecraftforge.fml.common.Loader

/**
 * @author Take Weiland
 */
interface CameraCraftApi {

    val isReal: Boolean

    val photographyEngine: PhotographyEngine

    val imageIO: ImageIO

    fun getPhotoStore(world: World): PhotoStore

    companion object INSTANCE : CameraCraftApi by getApiImpl()

}

private object DummyImpl : CameraCraftApi {
    override val imageIO: ImageIO
        get() {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    override fun getPhotoStore(world: World): PhotoStore {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override val isReal: Boolean
        get() = false

    override val photographyEngine: PhotographyEngine
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
}

private fun getApiImpl(): CameraCraftApi {
    val mod = Loader.instance().indexedModList["cameracraft"]
    return if (mod != null) {
        mod.mod as? CameraCraftApi ?: throw IllegalStateException("CameraCraft mod object does not implement CameraCraftApi.")
    } else {
        DummyImpl
    }
}