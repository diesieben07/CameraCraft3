package dev.weiland.mods.cameracraft

import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.fml.loading.FMLEnvironment

internal interface CCProxy {

    fun createViewport(entityId: Int)

    companion object : CCProxy by createProxyInstance()

}

private fun createProxyInstance(): CCProxy {
    val clsName = when (checkNotNull(FMLEnvironment.dist)) {
        Dist.CLIENT -> "dev.weiland.mods.cameracraft.client.ClientProxy"
        Dist.DEDICATED_SERVER -> "dev.weiland.mods.cameracraft.server.ServerProxy"
    }
    val cls = Class.forName(clsName)
    return cls.asSubclass(CCProxy::class.java).newInstance()
}