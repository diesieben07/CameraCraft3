package dev.weiland.mods.cameracraft

import dev.weiland.mods.cameracraft.blocks.CameraBlock
import dev.weiland.mods.cameracraft.blocks.TripodBlock
import dev.weiland.mods.cameracraft.items.CameraItem
import dev.weiland.mods.cameracraft.util.getValue
import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries

@Mod.EventBusSubscriber(modid = CameraCraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
internal object CCBlocks {

    val BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CameraCraft.MOD_ID)

    val TRIPOD by BLOCKS.register("tripod", ::TripodBlock)
    val CAMERA by BLOCKS.register("camera", ::CameraBlock)


    @JvmStatic
    @SubscribeEvent
    fun registerItems(evt: RegistryEvent.Register<Item>) {
        evt.registerItemBlock(TRIPOD) {
            maxStackSize(16)
        }
        evt.registerItemBlock(CAMERA, itemFactory = ::CameraItem) {
            maxStackSize(1)
        }
    }

    private fun RegistryEvent.Register<Item>.registerItemBlock(
            block: Block,
            itemFactory: (Block, Item.Properties) -> BlockItem = ::BlockItem,
            propertyBuilder: Item.Properties.() -> Unit = { },
    ) {
        registry.register(
            itemFactory(
                block,
                Item.Properties().also { it.group(CameraCraft.ITEM_GROUP) }.also { it.propertyBuilder() }
            ).also { it.registryName = block.registryName }
        )
    }

    fun init() {
        BLOCKS.register(FMLJavaModLoadingContext.get().modEventBus)
    }

}