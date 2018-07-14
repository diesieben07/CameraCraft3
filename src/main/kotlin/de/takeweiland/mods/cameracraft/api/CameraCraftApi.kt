package de.takeweiland.mods.cameracraft.api

import de.takeweiland.mods.cameracraft.api.photography.PhotographyEngine
import net.minecraftforge.fml.common.Loader

/**
 * @author Take Weiland
 */
interface CameraCraftApi {

    val isReal: Boolean

    val photographyEngine: PhotographyEngine

    companion object INSTANCE : CameraCraftApi by getApiImpl()

}

private object DummyImpl : CameraCraftApi {

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