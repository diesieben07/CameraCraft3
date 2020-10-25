package dev.weiland.mods.cameracraft

import dev.weiland.mods.cameracraft.util.getValue
import net.minecraft.block.*
import net.minecraft.block.material.Material
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.util.math.shapes.VoxelShapes
import net.minecraft.world.IBlockReader
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries

@Mod.EventBusSubscriber(modid = CameraCraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
internal object CCBlocks {

    private val BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CameraCraft.MOD_ID)

    val TRIPOD by BLOCKS.register("tripod") {
        object : BushBlock(AbstractBlock.Properties.create(Material.TALL_PLANTS).doesNotBlockMovement().zeroHardnessAndResistance()) {
            override fun propagatesSkylightDown(state: BlockState, reader: IBlockReader, pos: BlockPos): Boolean {
                return true
            }

            override fun getAmbientOcclusionLightValue(state: BlockState, worldIn: IBlockReader, pos: BlockPos): Float {
                return 0.2f
            }

            override fun getCollisionShape(state: BlockState, reader: IBlockReader, pos: BlockPos): VoxelShape {
                return VoxelShapes.empty()
            }
        }
    }

    @JvmStatic
    @SubscribeEvent
    fun registerItems(evt: RegistryEvent.Register<Item>) {
        for (block in BLOCKS.entries) {
            val item = BlockItem(block.get(), Item.Properties().group(CameraCraft.ITEM_GROUP))
            item.registryName = block.id
            evt.registry.register(item)
        }
    }

    fun init() {
        BLOCKS.register(FMLJavaModLoadingContext.get().modEventBus)
    }

}