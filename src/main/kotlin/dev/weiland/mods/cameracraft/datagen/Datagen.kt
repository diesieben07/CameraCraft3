package dev.weiland.mods.cameracraft.datagen

import dev.weiland.mods.cameracraft.CCBlocks
import dev.weiland.mods.cameracraft.CameraCraft
import net.minecraft.data.DataGenerator
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.generators.BlockModelProvider
import net.minecraftforge.client.model.generators.BlockStateProvider
import net.minecraftforge.client.model.generators.ItemModelProvider
import net.minecraftforge.common.data.ExistingFileHelper
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent

@Mod.EventBusSubscriber(modid = CameraCraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
internal object Datagen {

    @JvmStatic
    @SubscribeEvent
    fun onDatagen(evt: GatherDataEvent) {
        if (evt.includeClient()) {
            evt.generator.addProvider(BlockStates(evt.generator, evt.existingFileHelper))
            evt.generator.addProvider(ItemModels(evt.generator, evt.existingFileHelper))
        }
    }

    private class BlockStates(gen: DataGenerator?, exFileHelper: ExistingFileHelper?) : BlockStateProvider(gen, CameraCraft.MOD_ID, exFileHelper) {
        override fun registerStatesAndModels() {
            simpleBlock(
                CCBlocks.TRIPOD,
                models()
                    .cross(CCBlocks.TRIPOD.registryName!!.path, blockTexture(CCBlocks.TRIPOD))
                    .ao(false)
            )

        }
    }

    private class ItemModels(generator: DataGenerator?, existingFileHelper: ExistingFileHelper?) : ItemModelProvider(generator, CameraCraft.MOD_ID, existingFileHelper) {

        override fun registerModels() {
            withExistingParent(
                CCBlocks.TRIPOD.registryName!!.path, mcLoc("item/generated")
            )
                .texture("layer0", modLoc("${BLOCK_FOLDER}/${CCBlocks.TRIPOD.registryName!!.path}"))
        }
    }


}