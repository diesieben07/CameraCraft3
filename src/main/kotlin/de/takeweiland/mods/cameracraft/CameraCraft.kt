package de.takeweiland.mods.cameracraft

import de.takeweiland.mods.cameracraft.items.CAMERA
import de.takeweiland.mods.commons.fml.KOTLIN_LANGUAGE_ADAPTER
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import org.apache.logging.log4j.Logger

/**
 * @author Take Weiland
 */

internal const val CC_MODID = "cameracraft"
private const val NAME = "CameraCraft"
private const val VERSION = "0.0.1-alpha"

internal lateinit var LOG: Logger

internal val CC_CREATIVE_TAB = object : CreativeTabs("cameracraft") {
    override fun getTabIconItem(): ItemStack {
        return ItemStack(CAMERA)
    }
}

@Mod(modid = CC_MODID, name = NAME, version = VERSION, modLanguageAdapter = KOTLIN_LANGUAGE_ADAPTER, dependencies = "required-after:sevencommons")
internal object CameraCraft {

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        LOG = event.modLog

        LOG.info("Hi from CameraCraft!" )
    }

}