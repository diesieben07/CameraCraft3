package de.takeweiland.cameracraft

import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import org.apache.logging.log4j.Logger

/**
 * @author Take Weiland
 */

private const val ID = "cameracraft"
private const val NAME = "CameraCraft"
private const val VERSION = "0.0.1-alpha"

internal lateinit var LOG: Logger

@Mod(modid = ID, name = NAME, version = VERSION)
class CameraCraft {

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        LOG = event.modLog
    }

}