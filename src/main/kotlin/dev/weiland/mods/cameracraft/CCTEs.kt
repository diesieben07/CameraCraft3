package dev.weiland.mods.cameracraft

import dev.weiland.mods.cameracraft.blocks.CameraTile
import net.minecraft.tileentity.TileEntityType
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import dev.weiland.mods.cameracraft.util.getValue
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext

internal object CCTEs {

    val TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, CameraCraft.MOD_ID)

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    val CAMERA: TileEntityType<CameraTile> by TILE_ENTITIES.register("camera") {
        TileEntityType.Builder.of({ CameraTile() }, CCBlocks.CAMERA)
                .build(null)
    }

    fun init() {
        TILE_ENTITIES.register(FMLJavaModLoadingContext.get().modEventBus)
    }

}