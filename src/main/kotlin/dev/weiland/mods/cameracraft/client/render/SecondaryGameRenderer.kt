package dev.weiland.mods.cameracraft.client.render

import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.systems.RenderSystem
import dev.weiland.mods.cameracraft.CameraCraft
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.ActiveRenderInfo
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.client.settings.PointOfView
import net.minecraft.client.shader.Framebuffer
import net.minecraft.entity.LivingEntity
import net.minecraft.util.Util
import net.minecraft.util.math.vector.Matrix4f
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.EntityViewRenderEvent
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.EventPriority
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL30

@Mod.EventBusSubscriber(Dist.CLIENT, modid = CameraCraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
internal class SecondaryGameRenderer(private val mc: Minecraft, val entity: LivingEntity) {

    val imageWidth = 256
    val imageHeight = 256

    val frameBuffer: Framebuffer = Framebuffer(imageWidth, imageHeight, true, Minecraft.IS_RUNNING_ON_MAC)

    fun preRender() {
        val prevHUDState = mc.gameSettings.hideGUI
        val prevBobState = mc.gameSettings.viewBobbing
        val prevPointOfView = mc.gameSettings.pointOfView
        val prevViewport = mc.renderViewEntity
        val prevMainFBWidth = mc.mainWindow.framebufferWidth
        val prevMainFBHeight = mc.mainWindow.framebufferHeight

        mc.gameSettings.hideGUI = true
        mc.gameSettings.viewBobbing = false
        mc.gameSettings.pointOfView = PointOfView.FIRST_PERSON
        mc.renderViewEntity = entity
        mc.mainWindow.framebufferWidth = imageWidth
        mc.mainWindow.framebufferHeight = imageHeight

        RenderSystem.pushMatrix()
        inFakeRender = true
        mc.gameRenderer.updateCameraAndRender(if (mc.isGamePaused) mc.renderPartialTicksPaused else mc.renderPartialTicks, Util.nanoTime(), true)
        inFakeRender = false
        RenderSystem.popMatrix()

        mc.gameSettings.hideGUI = prevHUDState
        mc.gameSettings.viewBobbing = prevBobState
        mc.gameSettings.pointOfView = prevPointOfView
        mc.renderViewEntity = prevViewport

        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, mc.framebuffer.framebufferObject)
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, frameBuffer.framebufferObject)
        GL30.glBlitFramebuffer(
            0, 0, mc.framebuffer.framebufferWidth, mc.framebuffer.framebufferHeight,
            0, 0, imageWidth, imageHeight,
            GL30.GL_COLOR_BUFFER_BIT or GL30.GL_DEPTH_BUFFER_BIT or GL30.GL_STENCIL_BUFFER_BIT,
            GL30.GL_NEAREST
        )

        mc.mainWindow.framebufferWidth = prevMainFBWidth
        mc.mainWindow.framebufferHeight = prevMainFBHeight

        mc.framebuffer.bindFramebuffer(true)
    }

    fun render() {
        RenderSystem.pushMatrix()
        frameBuffer.framebufferRender(imageWidth, imageHeight)
        RenderSystem.popMatrix()
    }

    companion object {

        @JvmField var inFakeRender = false
        private var current: SecondaryGameRenderer? = null

        @JvmStatic
        @SubscribeEvent
        fun renderTick(evt: TickEvent.RenderTickEvent) {
            if (Minecraft.getInstance().world == null || Minecraft.getInstance().playerController == null) {
                current = null
            }
            if (evt.phase == TickEvent.Phase.END) {
                current?.render()
            } else {
                current?.preRender()
            }
        }

        @JvmStatic
        @SubscribeEvent(priority = EventPriority.LOWEST)
        fun fovModifierEvt(evt: EntityViewRenderEvent.FOVModifier) {
            // Force FOV to default when in a camera render
            if (inFakeRender) {
                evt.fov = 70.0
            }
        }

        @JvmStatic
        @SubscribeEvent(priority = EventPriority.LOWEST)
        fun preScreenRender(evt: GuiScreenEvent.DrawScreenEvent.Pre) {
            if (inFakeRender) {
                evt.isCanceled = true
            }
        }

        fun createOverlayForEntity(entity: LivingEntity) {
            if (current == null) {
                current = SecondaryGameRenderer(
                    Minecraft.getInstance(),
                    entity
                )
            }
        }

    }

}
