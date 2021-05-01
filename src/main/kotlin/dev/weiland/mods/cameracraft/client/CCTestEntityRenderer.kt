package dev.weiland.mods.cameracraft.client

import dev.weiland.mods.cameracraft.entity.CCViewportEntity
import net.minecraft.client.renderer.entity.EntityRendererManager
import net.minecraft.client.renderer.entity.LivingRenderer
import net.minecraft.client.renderer.entity.model.CreeperModel
import net.minecraft.util.ResourceLocation

internal class CCTestEntityRenderer(renderManager: EntityRendererManager) : LivingRenderer<CCViewportEntity, CreeperModel<CCViewportEntity>>(
    renderManager, CreeperModel(), 1F
) {

    override fun getTextureLocation(entity: CCViewportEntity): ResourceLocation {
        return ResourceLocation("textures/entity/creeper/creeper.png")
    }

}