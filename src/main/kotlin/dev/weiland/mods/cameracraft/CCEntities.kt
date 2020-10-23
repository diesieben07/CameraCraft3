package dev.weiland.mods.cameracraft

import dev.weiland.mods.cameracraft.entity.CCTestEntity
import net.minecraft.entity.EntityClassification
import net.minecraft.entity.EntityType
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries

internal object CCEntities {

    private val ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, CameraCraft.MOD_ID)
    val TEST_ENTITY = ENTITIES.register("test_entity") {
        EntityType.Builder.create(
            EntityType.IFactory<CCTestEntity> { type, world -> CCTestEntity(type, world) },
            EntityClassification.AMBIENT
        )
            .setShouldReceiveVelocityUpdates(true)
            .setTrackingRange(128)
            .setUpdateInterval(5)
            .build("${CameraCraft.MOD_ID}:test_entity")
    }

    fun init() {
        ENTITIES.register(FMLJavaModLoadingContext.get().modEventBus)
    }

}