package dev.weiland.mods.cameracraft.client.render

import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.ActiveRenderInfo
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.client.renderer.RenderTypeBuffers
import net.minecraft.entity.LivingEntity
import net.minecraft.potion.Effects
import net.minecraft.resources.IResourceManager
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.vector.Matrix4f
import net.minecraft.util.math.vector.Vector3f
import net.minecraftforge.client.ForgeHooksClient
import net.minecraftforge.fml.common.ObfuscationReflectionHelper

internal class FakeGameRenderer(
        private val mc: Minecraft,
        resourceManagerIn: IResourceManager,
        renderTypeBuffersIn: RenderTypeBuffers,
        private val width: Float,
        private val height: Float,
        private val entity: LivingEntity
) : GameRenderer(mc, resourceManagerIn, renderTypeBuffersIn) {

    init {
        activeRenderInfo.previousHeight = entity.eyeHeight
        activeRenderInfo.height = entity.eyeHeight

        // set renderHand to false
        ObfuscationReflectionHelper.setPrivateValue(
                GameRenderer::class.java, this, false, "field_175074_C"
        )

        // set farPlaneDistance
        val chunkRenderDistance = 4
        ObfuscationReflectionHelper.setPrivateValue(
                GameRenderer::class.java, this, chunkRenderDistance * 16, "field_78530_s"
        )
    }

    override fun getProjectionMatrix(activeRenderInfoIn: ActiveRenderInfo, partialTicks: Float, useFovSetting: Boolean): Matrix4f {
        val matrixstack = MatrixStack()
        matrixstack.last.matrix.setIdentity()
        matrixstack.last.matrix.mul(Matrix4f.perspective(70.0, width / height, 0.05f, farPlaneDistance * 4.0f))
        return matrixstack.last.matrix
    }

    override fun renderWorld(partialTicks: Float, finishTimeNano: Long, matrixStackIn: MatrixStack) {
        val world = mc.world ?: return

        lightmapTexture.updateLightmap(partialTicks)
        if (mc.getRenderViewEntity() == null) {
            mc.setRenderViewEntity(entity)
        }
        mc.profiler.endStartSection("camera")
        val activerenderinfo = activeRenderInfo
        val matrixstack = MatrixStack()
        matrixstack.last.matrix.mul(getProjectionMatrix(activerenderinfo, partialTicks, true))

        val matrix4f = matrixstack.last.matrix
        resetProjectionMatrix(matrix4f)
        activerenderinfo.update(world, mc.renderViewEntity ?: entity, false, false, partialTicks)

        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(activerenderinfo.pitch))
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(activerenderinfo.yaw + 180.0f))

        mc.worldRenderer.updateCameraAndRender(matrixStackIn, partialTicks, finishTimeNano, false, activerenderinfo, this, lightmapTexture, matrix4f)

        // TODO: decide if we want this event
//        mc.profiler.endStartSection("forge_render_last")
//        ForgeHooksClient.dispatchRenderLast(mc.worldRenderer, matrixStackIn, partialTicks, matrix4f, finishTimeNano)
        mc.profiler.endSection()
    }

}