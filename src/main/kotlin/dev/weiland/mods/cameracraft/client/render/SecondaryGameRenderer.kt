package dev.weiland.mods.cameracraft.client.render

import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.systems.RenderSystem
import dev.weiland.mods.cameracraft.CameraCraft
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.ActiveRenderInfo
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.client.shader.Framebuffer
import net.minecraft.entity.LivingEntity
import net.minecraft.util.Util
import net.minecraft.util.math.vector.Matrix4f
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.EntityViewRenderEvent
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.EventPriority
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL30

@Mod.EventBusSubscriber(Dist.CLIENT, modid = CameraCraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
internal class SecondaryGameRenderer(private val mc: Minecraft, val entity: LivingEntity) {

    val frameBuffer: Framebuffer = Framebuffer(256, 256, true, Minecraft.IS_RUNNING_ON_MAC)

    fun preRender() {
        val prevHUDState = mc.gameSettings.hideGUI
        val prevBobState = mc.gameSettings.viewBobbing
        val prevViewport = mc.renderViewEntity

        mc.gameSettings.hideGUI = true
        mc.gameSettings.viewBobbing = false
        mc.renderViewEntity = entity

        RenderSystem.pushMatrix()
        inFakeRender = true
        mc.gameRenderer.updateCameraAndRender(if (mc.isGamePaused) mc.renderPartialTicksPaused else mc.renderPartialTicks, Util.nanoTime(), true)
        inFakeRender = false
        RenderSystem.popMatrix()

        mc.gameSettings.hideGUI = prevHUDState
        mc.gameSettings.viewBobbing = prevBobState
        mc.renderViewEntity = prevViewport

        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, mc.framebuffer.framebufferObject)
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, frameBuffer.framebufferObject)
        GL30.glBlitFramebuffer(
            0, 0, mc.framebuffer.framebufferWidth, mc.framebuffer.framebufferHeight,
            0, 0, frameBuffer.framebufferWidth, frameBuffer.framebufferHeight,
            GL30.GL_COLOR_BUFFER_BIT or GL30.GL_DEPTH_BUFFER_BIT or GL30.GL_STENCIL_BUFFER_BIT,
            GL30.GL_NEAREST
        )

        mc.framebuffer.bindFramebuffer(true)
    }

    fun render() {
        RenderSystem.pushMatrix()
        frameBuffer.framebufferRender(256, 256)
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

        fun doRender() {
            current?.render()
        }

        fun fakeRenderProjectionMatrix(gameRenderer: GameRenderer, activeRenderInfoIn: ActiveRenderInfo?, partialTicks: Float, useFovSetting: Boolean): Matrix4f {
            with (gameRenderer) {
                val matrixstack = MatrixStack()
                matrixstack.last.matrix.setIdentity()

                matrixstack.last.matrix.mul(
                    Matrix4f.perspective(
                        70.0 + (System.currentTimeMillis() % 10_000L).toFloat() / (10_000f / 70f),
                        256f / 256f, 0.05f,
                        this.farPlaneDistance * 4.0f
                    )
                )
                return matrixstack.last.matrix
            }
        }

        fun createOverlayForEntity(entity: LivingEntity) {
            current = SecondaryGameRenderer(
                Minecraft.getInstance(),
                entity
            )
        }

    }

}
