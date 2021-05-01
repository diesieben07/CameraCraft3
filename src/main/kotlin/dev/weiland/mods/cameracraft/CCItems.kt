package dev.weiland.mods.cameracraft

import dev.weiland.mods.cameracraft.items.TripodMinecartItem
import dev.weiland.mods.cameracraft.util.getValue
import net.minecraft.item.Item
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries

internal object CCItems {

    val ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CameraCraft.MOD_ID)
    val TRIPOD_MINECART by ITEMS.register("tripod_minecart") {
        TripodMinecartItem(Item.Properties().tab(CameraCraft.ITEM_GROUP))
    }

    fun init() {
        ITEMS.register(FMLJavaModLoadingContext.get().modEventBus)
    }

}