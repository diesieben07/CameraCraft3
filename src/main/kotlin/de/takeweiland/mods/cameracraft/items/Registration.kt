@file:Mod.EventBusSubscriber
package de.takeweiland.mods.cameracraft.items

import de.takeweiland.mods.cameracraft.CC_CREATIVE_TAB
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.item.Item
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

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

@SideOnly(Side.CLIENT)
internal fun registerModels(event: ModelRegistryEvent) {
    ModelLoader.setCustomModelResourceLocation(CAMERA, 0, ModelResourceLocation(CAMERA.registryName, "inventory"))
}