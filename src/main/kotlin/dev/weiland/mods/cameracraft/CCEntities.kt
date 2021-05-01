package dev.weiland.mods.cameracraft

import dev.weiland.mods.cameracraft.util.getValue
import dev.weiland.mods.cameracraft.entity.CCViewportEntity
import dev.weiland.mods.cameracraft.entity.TripodMinecartEntity
import net.minecraft.entity.EntityClassification
import net.minecraft.entity.EntityType
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries

internal object CCEntities {

    private val ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, CameraCraft.MOD_ID)
    val TEST_ENTITY = ENTITIES.register("test_entity") {
        EntityType.Builder.of(
            EntityType.IFactory<CCViewportEntity> { type, world -> CCViewportEntity(type, world) },
            EntityClassification.AMBIENT
        )
            .setShouldReceiveVelocityUpdates(true)
            .setTrackingRange(128)
            .setUpdateInterval(5)
            .build("${CameraCraft.MOD_ID}:test_entity")
    }
    val TRIPOD_MINECART: EntityType<TripodMinecartEntity> by ENTITIES.register("tripod_minecart") {
        EntityType.Builder.of<TripodMinecartEntity>(
                { type, world -> TripodMinecartEntity(type, world) },
                EntityClassification.MISC
        ).sized(0.98f, 1f).clientTrackingRange(8).build("${CameraCraft.MOD_ID}:tripod_minecart")
    }

    fun init() {
        ENTITIES.register(FMLJavaModLoadingContext.get().modEventBus)
    }

}