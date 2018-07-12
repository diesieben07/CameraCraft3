@file:Mod.EventBusSubscriber
package de.takeweiland.mods.cameracraft.items

import de.takeweiland.mods.cameracraft.CC_CREATIVE_TAB
import net.minecraft.item.Item
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@JvmField
internal val CAMERA = ItemCamera()

private fun Item.setNames(name: String) {
    setRegistryName(name)
    unlocalizedName = registryName.toString()
}

@SubscribeEvent
internal fun registerItems(event: RegistryEvent.Register<Item>) {
    with (CAMERA) {
        setNames("camera")
        setCreativeTab(CC_CREATIVE_TAB)
    }

    event.registry.registerAll(CAMERA)
}