package dev.weiland.mods.cameracraft.client

import dev.weiland.mods.cameracraft.CCProxy
import dev.weiland.mods.cameracraft.CameraCraft
import dev.weiland.mods.cameracraft.client.fakeworld.render.SecondaryGameRenderer
import net.minecraft.client.Minecraft
import net.minecraft.entity.LivingEntity

@Suppress("unused")
internal class ClientProxy : CCProxy {

    override fun createOverlayForEntity(entityId: Int) {
        val entity = Minecraft.getInstance().world?.getEntityByID(entityId) as? LivingEntity
        if (entity == null) {
           CameraCraft.LOGGER.warn("Invalid overlay entityId received")
        } else {
            SecondaryGameRenderer.createOverlayForEntity(entity)
        }
    }
}