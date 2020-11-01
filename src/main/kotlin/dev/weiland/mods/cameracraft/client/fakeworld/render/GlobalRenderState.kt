package dev.weiland.mods.cameracraft.client.fakeworld.render

import net.minecraft.client.Minecraft
import net.minecraft.client.settings.PointOfView
import net.minecraft.entity.Entity

internal class GlobalRenderState(
//    private val viewBobbing: Boolean,
    private val pointOfView: PointOfView,
    private val viewportEntity: Entity?,
//    private val mainFramebufferWidth: Int,
//    private val mainFramebufferHeight: Int,
) : GlobalState {

    override fun restore(mc: Minecraft) {
//        mc.gameSettings.viewBobbing = viewBobbing
        mc.gameSettings.pointOfView = pointOfView
        mc.renderViewEntity = viewportEntity
//        mc.mainWindow.framebufferWidth = mainFramebufferWidth
//        mc.mainWindow.framebufferHeight = mainFramebufferHeight
    }

    companion object {
        fun capture(mc: Minecraft): GlobalRenderState {
            return GlobalRenderState(
//                viewBobbing = mc.gameSettings.viewBobbing,
                pointOfView = mc.gameSettings.pointOfView,
                viewportEntity = mc.renderViewEntity,
//                mainFramebufferWidth = mc.mainWindow.framebufferWidth,
//                mainFramebufferHeight = mc.mainWindow.framebufferHeight,
            )
        }
    }

}