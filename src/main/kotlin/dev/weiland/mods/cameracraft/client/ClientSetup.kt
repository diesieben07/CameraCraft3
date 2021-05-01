package dev.weiland.mods.cameracraft.client

import dev.weiland.mods.cameracraft.CCBlocks
import dev.weiland.mods.cameracraft.CCEntities
import dev.weiland.mods.cameracraft.CameraCraft
import dev.weiland.mods.cameracraft.client.entity.TripodMinecartRenderer
import dev.weiland.mods.cameracraft.client.model.CameraModelLoader
import dev.weiland.mods.cameracraft.client.model.RotationModelLoader
import dev.weiland.mods.cameracraft.entity.TripodMinecartEntity
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.RenderTypeLookup
import net.minecraft.client.renderer.entity.MinecartRenderer
import net.minecraft.util.ResourceLocation
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.client.model.ModelLoaderRegistry
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.client.registry.RenderingRegistry
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent

@Mod.EventBusSubscriber(Dist.CLIENT, modid = CameraCraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
internal object ClientSetup {

    @JvmStatic
    @SubscribeEvent
    fun clientSetup(event: FMLClientSetupEvent) {
        RenderTypeLookup.setRenderLayer(CCBlocks.TRIPOD, RenderType.cutout())

        RenderingRegistry.registerEntityRenderingHandler(CCEntities.TEST_ENTITY.get(), ::CCTestEntityRenderer)
        RenderingRegistry.registerEntityRenderingHandler(CCEntities.TRIPOD_MINECART) { TripodMinecartRenderer(it) }
    }

    @JvmStatic
    @SubscribeEvent
    fun modelRegistry(event: ModelRegistryEvent) {
        ModelLoaderRegistry.registerLoader(RotationModelLoader.ID, RotationModelLoader)
        ModelLoaderRegistry.registerLoader(CameraModelLoader.ID, CameraModelLoader)
    }

}