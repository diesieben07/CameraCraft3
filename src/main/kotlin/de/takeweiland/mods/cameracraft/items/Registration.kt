@file:Mod.EventBusSubscriber
package de.takeweiland.mods.cameracraft.items

import de.takeweiland.mods.cameracraft.CC_CREATIVE_TAB
import de.takeweiland.mods.cameracraft.CC_MODID
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.item.Item
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.registry.ForgeRegistries
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
@SubscribeEvent
internal fun registerModels(event: ModelRegistryEvent) {
    for (item in ForgeRegistries.ITEMS) {
        if (item.registryName!!.resourceDomain == CC_MODID) {
            ModelLoader.setCustomModelResourceLocation(item, 0, ModelResourceLocation(item.registryName!!, "inventory"))
        }
    }

}