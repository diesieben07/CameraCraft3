package dev.weiland.mods.cameracraft.client

import dev.weiland.mods.cameracraft.CCBlocks
import dev.weiland.mods.cameracraft.CCEntities
import dev.weiland.mods.cameracraft.CameraCraft
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.RenderTypeLookup
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.client.registry.RenderingRegistry
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent

@Mod.EventBusSubscriber(Dist.CLIENT, modid = CameraCraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
internal object ClientSetup {

    @JvmStatic
    @SubscribeEvent
    fun clientSetup(event: FMLClientSetupEvent) {
        RenderingRegistry.registerEntityRenderingHandler(CCEntities.TEST_ENTITY.get(), ::CCTestEntityRenderer)
        RenderTypeLookup.setRenderLayer(CCBlocks.TRIPOD, RenderType.getCutout())

    }

}