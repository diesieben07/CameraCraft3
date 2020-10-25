package dev.weiland.mods.cameracraft.client.render

import com.mojang.blaze3d.systems.RenderSystem
import dev.weiland.mods.cameracraft.CameraCraft
import net.minecraft.client.Minecraft
import net.minecraft.client.settings.PointOfView
import net.minecraft.client.shader.Framebuffer
import net.minecraft.entity.LivingEntity
import net.minecraft.util.Util
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.EntityViewRenderEvent
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.EventPriority
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

@Mod.EventBusSubscriber(Dist.CLIENT, modid = CameraCraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
internal class SecondaryGameRenderer(private val mc: Minecraft, val entity: LivingEntity) {

    val imageWidth = /*mc.mainWindow.framebufferWidth*/ 256
    val imageHeight = /*mc.mainWindow.framebufferHeight*/ 256

    val frameBuffer: Framebuffer = Framebuffer(imageWidth, imageHeight, true, Minecraft.IS_RUNNING_ON_MAC)

    val globalRenderState = GlobalRenderState(
        hideHUD = true,
        viewBobbing = false,
        pointOfView = PointOfView.FIRST_PERSON,
        viewportEntity = entity,
        mainFramebufferWidth = imageWidth,
        mainFramebufferHeight = imageHeight,
        mainFramebuffer = frameBuffer,
        activeRenderPrevHeight = entity.eyeHeight,
        activeRenderHeight = entity.eyeHeight
    )

    fun preRender(): Boolean {
        if (!entity.isAlive) {
            return false
        }

        val savedRenderState = GlobalRenderState.capture(mc)

        globalRenderState.restore(mc)

        mc.framebuffer.bindFramebuffer(true)

        RenderSystem.pushMatrix()
        inFakeRender = true
        mc.gameRenderer.updateCameraAndRender(if (mc.isGamePaused) mc.renderPartialTicksPaused else mc.renderPartialTicks, Util.nanoTime(), true)
        inFakeRender = false
        RenderSystem.popMatrix()

        savedRenderState.restore(mc)

        mc.framebuffer.bindFramebuffer(true)

        return true
    }

    fun render() {
        RenderSystem.pushMatrix()
        frameBuffer.framebufferRender(256, 256)
        RenderSystem.popMatrix()
    }

    companion object {

        @JvmField
        var inFakeRender = false

        private var active: Array<SecondaryGameRenderer> = emptyArray()

        private var current: SecondaryGameRenderer? = null

        @JvmStatic
        @SubscribeEvent
        fun renderTick(evt: TickEvent.RenderTickEvent) {
            if (Minecraft.getInstance().world == null || Minecraft.getInstance().playerController == null || current?.entity?.isAlive == false) {
                current?.frameBuffer?.deleteFramebuffer()
                current = null
            }
            if (evt.phase == TickEvent.Phase.END) {
                current?.render()
            } else {
                current?.preRender()
            }
        }


        @JvmStatic
        @SubscribeEvent
        fun clientTick(evt: TickEvent.ClientTickEvent) {
            if (evt.phase == TickEvent.Phase.START) {
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
            current?.frameBuffer?.deleteFramebuffer()
            current = SecondaryGameRenderer(
                Minecraft.getInstance(),
                entity
            )
        }

    }

}
