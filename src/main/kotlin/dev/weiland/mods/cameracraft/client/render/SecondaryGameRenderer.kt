package dev.weiland.mods.cameracraft.client.render

import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.systems.RenderSystem
import dev.weiland.mods.cameracraft.CameraCraft
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.client.shader.Framebuffer
import net.minecraft.entity.LivingEntity
import net.minecraft.util.Util
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL30

@Mod.EventBusSubscriber(Dist.CLIENT, modid = CameraCraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
internal class SecondaryGameRenderer(private val mc: Minecraft, val entity: LivingEntity) {

    val frameBuffer: Framebuffer = Framebuffer(256, 256, true, Minecraft.IS_RUNNING_ON_MAC)

//    val gameRenderer: GameRenderer = object : GameRenderer(
//        mc, mc.resourceManager, mc.renderTypeBuffers
//    ) {
//
//        override fun applyBobbing(p_228383_1_: MatrixStack, p_228383_2_: Float) {
//
//        }
//
//        override fun getMouseOver(p_78473_1_: Float) {
//
//        }
//
//        override fun getProjectionMatrix(activeRenderInfoIn: ActiveRenderInfo, partialTicks: Float, useFovSetting: Boolean): Matrix4f {
//            val matrixstack = MatrixStack()
//            matrixstack.last.matrix.setIdentity()
//            if (cameraZoom != 1.0f) {
//                matrixstack.translate(cameraYaw.toDouble(), (-cameraPitch).toDouble(), 0.0)
//                matrixstack.scale(cameraZoom, cameraZoom, 1.0f)
//            }
//
//            matrixstack.last.matrix.mul(
//                Matrix4f.perspective(
//                    getFOVModifier(activeRenderInfoIn, partialTicks, useFovSetting),
//                    this@SecondaryGameRenderer.frameBuffer.framebufferWidth.toFloat() / this@SecondaryGameRenderer.frameBuffer.framebufferHeight.toFloat(),
//                    0.05f, farPlaneDistance * 4.0f
//                )
//            )
//            return matrixstack.last.matrix
//        }
//    }

    fun render_old() {
        val prevHudSetting = mc.gameSettings.hideGUI
        val prevPOV = mc.renderViewEntity

        mc.gameSettings.viewBobbing = false
        val fbBackup = mc.framebuffer
        mc.framebuffer = frameBuffer
//        mc.gameSettings.hideGUI = true
//        mc.renderViewEntity = entity


        val nanoTime = Util.nanoTime()
        try {
            frameBuffer.bindFramebuffer(true)

            GlStateManager.matrixMode(GL11.GL_PROJECTION)
            GlStateManager.loadIdentity()
            GlStateManager.ortho(0.0, 256.0, 256.0, 0.0, 1000.0, 3000.0)

            GlStateManager.matrixMode(GL11.GL_MODELVIEW)
            GlStateManager.loadIdentity()
            GlStateManager.translatef(0.0f, 0.0f, -2000.0f)

            GlStateManager.viewport(0, 0, 256, 256)



//            FogRenderer.resetFog()
//            RenderSystem.enableTexture()
//            RenderSystem.enableCull()
            val matrixStack = MatrixStack()
            mc.gameRenderer.renderWorld(if (mc.isGamePaused) mc.renderPartialTicksPaused else mc.renderPartialTicks, nanoTime, matrixStack)

            println(
                "Bound: ${GL30.glGetInteger(GL30.GL_FRAMEBUFFER_BINDING)}\n" +
                        "Mine: ${frameBuffer.framebufferObject}"
            )


            mc.framebuffer = fbBackup
            mc.framebuffer.bindFramebuffer(true)

            frameBuffer.framebufferRender(256, 256)
        } finally {
            mc.gameSettings.hideGUI = prevHudSetting
            mc.renderViewEntity = prevPOV
        }

    }

    fun preRender() {
        RenderSystem.pushMatrix()
        mc.gameRenderer.updateCameraAndRender(if (mc.isGamePaused) mc.renderPartialTicksPaused else mc.renderPartialTicks, Util.nanoTime(), true)
        RenderSystem.popMatrix()

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

        fun doRender() {
            current?.render()
        }

        fun createOverlayForEntity(entity: LivingEntity) {
            current = SecondaryGameRenderer(
                Minecraft.getInstance(),
                entity
            )
        }

    }

}

private fun Framebuffer.framebufferRenderExtRaw(width: Int, height: Int, p_227588_3_: Boolean) {
    RenderSystem.assertThread { RenderSystem.isOnRenderThread() }
    GlStateManager.colorMask(true, true, true, false)
    GlStateManager.disableDepthTest()
    GlStateManager.depthMask(false)
    GlStateManager.matrixMode(GL11.GL_PROJECTION)
    GlStateManager.loadIdentity()
    GlStateManager.ortho(0.0, width.toDouble(), height.toDouble(), 0.0, 1000.0, 3000.0)
    GlStateManager.matrixMode(GL11.GL_MODELVIEW)
    GlStateManager.loadIdentity()
    GlStateManager.translatef(0.0f, 0.0f, -2000.0f)
    GlStateManager.viewport(0, 0, width, height)
    GlStateManager.enableTexture()
    GlStateManager.disableLighting()
    GlStateManager.disableAlphaTest()
    if (p_227588_3_) {
        GlStateManager.disableBlend()
        GlStateManager.enableColorMaterial()
    }
    GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f)
    this.bindFramebufferTexture()
    val f = width.toFloat()
    val f1 = height.toFloat()
    val f2 = this.framebufferWidth.toFloat() / this.framebufferTextureWidth.toFloat()
    val f3 = this.framebufferHeight.toFloat() / this.framebufferTextureHeight.toFloat()
    val tessellator = RenderSystem.renderThreadTesselator()
    val bufferbuilder = tessellator.buffer
    bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR)
    bufferbuilder.pos(0.0, f1.toDouble(), 0.0).tex(0.0f, 0.0f).color(255, 255, 255, 255).endVertex()
    bufferbuilder.pos(f.toDouble(), f1.toDouble(), 0.0).tex(f2, 0.0f).color(255, 255, 255, 255).endVertex()
    bufferbuilder.pos(f.toDouble(), 0.0, 0.0).tex(f2, f3).color(255, 255, 255, 255).endVertex()
    bufferbuilder.pos(0.0, 0.0, 0.0).tex(0.0f, f3).color(255, 255, 255, 255).endVertex()
    tessellator.draw()
    this.unbindFramebufferTexture()
    GlStateManager.depthMask(true)
    GlStateManager.colorMask(true, true, true, true)
}