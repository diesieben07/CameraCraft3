package dev.weiland.mods.cameracraft.datagen

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.mojang.datafixers.util.Pair
import dev.weiland.mods.cameracraft.CCBlocks
import dev.weiland.mods.cameracraft.CameraCraft
import dev.weiland.mods.cameracraft.blocks.CameraBlock
import net.minecraft.block.Block
import net.minecraft.data.DataGenerator
import net.minecraft.data.LootTableProvider
import net.minecraft.loot.LootParameterSet
import net.minecraft.loot.LootParameterSets
import net.minecraft.loot.LootTable
import net.minecraft.loot.ValidationTracker
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.vector.Quaternion
import net.minecraftforge.client.model.generators.BlockModelBuilder
import net.minecraftforge.client.model.generators.BlockStateProvider
import net.minecraftforge.client.model.generators.ConfiguredModel
import net.minecraftforge.client.model.generators.ItemModelProvider
import net.minecraftforge.common.data.ExistingFileHelper
import net.minecraftforge.common.data.LanguageProvider
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent
import java.util.function.BiConsumer
import java.util.function.Consumer
import java.util.function.Supplier

@Mod.EventBusSubscriber(modid = CameraCraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
internal object Datagen {

    @JvmStatic
    @SubscribeEvent
    fun onDatagen(evt: GatherDataEvent) {
        if (evt.includeClient()) {
            evt.generator.addProvider(BlockStates(evt.generator, evt.existingFileHelper))
            evt.generator.addProvider(ItemModels(evt.generator, evt.existingFileHelper))
        }

        if (evt.includeServer()) {
            evt.generator.addProvider(LootTables(evt.generator))
        }

        evt.generator.addProvider(Language(evt.generator, "en_us"))
    }

    private class FixedBlockModelBuilder(
        private val json: JsonObject,
        outputLocation: ResourceLocation?,
        existingFileHelper: ExistingFileHelper?
    ) : BlockModelBuilder(outputLocation, existingFileHelper) {
        override fun toJson(): JsonObject {
            return json
        }
    }

    private class TransformBlockModelBuilder(
        outputLocation: ResourceLocation?,
        existingFileHelper: ExistingFileHelper?,
        val delegate: BlockModelBuilder,
        val degrees: Float,
    ) : BlockModelBuilder(outputLocation, existingFileHelper) {


        override fun toJson(): JsonObject {
            val json = delegate.toJson()
            val transform = JsonObject()
            transform.addProperty("origin", "center")
            val rotation = Quaternion(0f, degrees, 0f, true)
            transform.add("rotation", JsonArray().also { array ->
                array.add(rotation.x)
                array.add(rotation.y)
                array.add(rotation.z)
                array.add(rotation.w)
            })
            json.add("transform", transform)
            return json
        }

    }


    private class BlockStates(gen: DataGenerator?, val exFileHelper: ExistingFileHelper?) : BlockStateProvider(gen, CameraCraft.MOD_ID, exFileHelper) {
        override fun registerStatesAndModels() {
            simpleBlock(
                CCBlocks.TRIPOD,
                models()
                    .cross(CCBlocks.TRIPOD.registryName!!.path, blockTexture(CCBlocks.TRIPOD))
                    .ao(false)
            )

//            val baseModel = models().withExistingParent("camera_temp", modLoc("camera"))
//                .transforms().transform()
//            models().generatedModels.remove(baseModel.location)
//            val baseModelJson = baseModel.toJson()

            getVariantBuilder(CCBlocks.CAMERA)
                .forAllStates { state ->
                    val rotation = state[CameraBlock.ROTATION]

//                    val modelJson = JsonObject()
//                    modelJson.addProperty("loader", RotationModelLoader.ID.toString())
//                    modelJson.addProperty("rotation_y", rotation.toFloat() * 10f)
//                    modelJson.add("model", baseModelJson)
//
//
//                    val modelLocation = ResourceLocation(CameraCraft.MOD_ID, "${ModelProvider.BLOCK_FOLDER}/camera_${rotation}")
//                    val model = FixedBlockModelBuilder(modelJson, modelLocation, exFileHelper)
//                    models().generatedModels[modelLocation] = model

                    val model = models().withExistingParent("camera_$rotation", modLoc("camera"))
                    val realModel = TransformBlockModelBuilder(model.location, exFileHelper, model, rotation.toFloat() * 10f)
                    models().generatedModels[realModel.location] = realModel

                    ConfiguredModel.builder().modelFile(realModel).build()
                }

//            simpleBlock(
//                CCBlocks.CAMERA,
//                models().getExistingFile(modLoc("camera"))
//            )

        }
    }

    private class ItemModels(generator: DataGenerator?, existingFileHelper: ExistingFileHelper?) : ItemModelProvider(
        generator, CameraCraft.MOD_ID, existingFileHelper
    ) {

        override fun registerModels() {
            singleTexture(
                CCBlocks.TRIPOD.registryName!!.path, mcLoc("item/generated"),
                "layer0", modLoc("${BLOCK_FOLDER}/${CCBlocks.TRIPOD.registryName!!.path}")
            )
            singleTexture(
                CCBlocks.CAMERA.registryName!!.path, mcLoc("item/generated"),
                "layer0", modLoc("${BLOCK_FOLDER}/${CCBlocks.CAMERA.registryName!!.path}")
            )
        }
    }

    private class LootTables(gen: DataGenerator) : LootTableProvider(gen) {

        override fun getTables(): List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> {
            return listOf(
                Pair.of(
                    Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>> {
                        BlockLootTables()
                    },
                    LootParameterSets.BLOCK
                )
            )
        }

        override fun validate(map: MutableMap<ResourceLocation, LootTable>, validationtracker: ValidationTracker) {

        }
    }

    private class BlockLootTables : net.minecraft.data.loot.BlockLootTables() {

        override fun getKnownBlocks(): Iterable<Block> {
            return CCBlocks.BLOCKS.entries.map { it.get() }
        }

        override fun addTables() {
            registerDropSelfLootTable(CCBlocks.CAMERA)
            registerDropSelfLootTable(CCBlocks.TRIPOD)
        }
    }

    private class Language(gen: DataGenerator?, locale: String?) : LanguageProvider(gen, CameraCraft.MOD_ID, locale) {

        override fun addTranslations() {
            add(CCBlocks.CAMERA, "Camera")
            add(CCBlocks.TRIPOD, "Tripod")
        }
    }

}