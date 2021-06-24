package dev.weiland.mods.cameracraft.client.fakeworld.render

import dev.weiland.mods.cameracraft.mixin.ClientPlayNetHandlerAccessor
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.client.renderer.WorldRenderer
import net.minecraft.client.settings.PointOfView
import net.minecraft.client.world.ClientWorld
import net.minecraft.entity.Entity

internal class GlobalRenderState(
    private val cameraType: PointOfView,
    private val cameraEntity: Entity?,
    private val levelRenderer: WorldRenderer,
    private val gameRenderer: GameRenderer,
    private val level: ClientWorld?,
) : GlobalState {

    override fun restore(mc: Minecraft) {
        mc.options.cameraType = cameraType
        mc.cameraEntity = cameraEntity
        mc.levelRenderer = levelRenderer
        mc.gameRenderer = gameRenderer
        mc.level = level
        (mc.connection as ClientPlayNetHandlerAccessor).setLevel(level)
        (mc.connection as ClientPlayNetHandlerAccessor).setLevelData(level?.levelData)
    }

    companion object {
        fun capture(mc: Minecraft): GlobalRenderState {
            return GlobalRenderState(
                cameraType = mc.options.cameraType,
                cameraEntity = mc.cameraEntity,
                levelRenderer = mc.levelRenderer,
                gameRenderer = mc.gameRenderer,
                level = mc.level
            )
        }
    }

}