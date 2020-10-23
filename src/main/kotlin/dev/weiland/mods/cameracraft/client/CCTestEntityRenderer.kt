package dev.weiland.mods.cameracraft.client

import dev.weiland.mods.cameracraft.entity.CCTestEntity
import net.minecraft.client.renderer.entity.EntityRendererManager
import net.minecraft.client.renderer.entity.LivingRenderer
import net.minecraft.client.renderer.entity.model.CreeperModel
import net.minecraft.entity.LivingEntity
import net.minecraft.util.ResourceLocation

internal class CCTestEntityRenderer(renderManager: EntityRendererManager) : LivingRenderer<CCTestEntity, CreeperModel<CCTestEntity>>(
    renderManager, CreeperModel(), 1F
) {

    override fun getEntityTexture(entity: CCTestEntity): ResourceLocation {
        return ResourceLocation("textures/entity/creeper/creeper.png")
    }

}