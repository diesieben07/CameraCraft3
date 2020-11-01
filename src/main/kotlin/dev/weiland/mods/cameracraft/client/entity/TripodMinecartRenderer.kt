package dev.weiland.mods.cameracraft.client.entity

import com.mojang.blaze3d.matrix.MatrixStack
import dev.weiland.mods.cameracraft.entity.TripodMinecartEntity
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.entity.EntityRendererManager
import net.minecraft.client.renderer.entity.MinecartRenderer
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.item.BlockItem
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.vector.Vector3d
import net.minecraft.util.math.vector.Vector3f

internal class TripodMinecartRenderer(rendererManager: EntityRendererManager) : MinecartRenderer<TripodMinecartEntity>(rendererManager) {

    override fun render(entityIn: TripodMinecartEntity, entityYaw: Float, partialTicks: Float, matrixStackIn: MatrixStack, bufferIn: IRenderTypeBuffer, packedLightIn: Int) {
        var entityYaw = entityYaw
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn)
        matrixStackIn.push()
        var i = entityIn.entityId.toLong() * 493286711L
        i = i * i * 4392167121L + i * 98761L
        val f = (((i shr 16 and 7L).toFloat() + 0.5f) / 8.0f - 0.5f) * 0.004f
        val f1 = (((i shr 20 and 7L).toFloat() + 0.5f) / 8.0f - 0.5f) * 0.004f
        val f2 = (((i shr 24 and 7L).toFloat() + 0.5f) / 8.0f - 0.5f) * 0.004f
        matrixStackIn.translate(f.toDouble(), f1.toDouble(), f2.toDouble())
        val d0 = MathHelper.lerp(partialTicks.toDouble(), entityIn.lastTickPosX, entityIn.getPosX())
        val d1 = MathHelper.lerp(partialTicks.toDouble(), entityIn.lastTickPosY, entityIn.getPosY())
        val d2 = MathHelper.lerp(partialTicks.toDouble(), entityIn.lastTickPosZ, entityIn.getPosZ())
        val d3 = 0.3f.toDouble()
        val vector3d: Vector3d? = entityIn.getPos(d0, d1, d2)
        var f3 = MathHelper.lerp(partialTicks, entityIn.prevRotationPitch, entityIn.rotationPitch)
        if (vector3d != null) {
            var vector3d1: Vector3d? = entityIn.getPosOffset(d0, d1, d2, 0.3f.toDouble())
            var vector3d2: Vector3d? = entityIn.getPosOffset(d0, d1, d2, (-0.3f).toDouble())
            if (vector3d1 == null) {
                vector3d1 = vector3d
            }
            if (vector3d2 == null) {
                vector3d2 = vector3d
            }
            matrixStackIn.translate(vector3d.x - d0, (vector3d1.y + vector3d2.y) / 2.0 - d1, vector3d.z - d2)
            var vector3d3 = vector3d2.add(-vector3d1.x, -vector3d1.y, -vector3d1.z)
            if (vector3d3.length() != 0.0) {
                vector3d3 = vector3d3.normalize()
                entityYaw = (Math.atan2(vector3d3.z, vector3d3.x) * 180.0 / Math.PI).toFloat()
                f3 = (Math.atan(vector3d3.y) * 73.0).toFloat()
            }
        }
        matrixStackIn.translate(0.0, 0.375, 0.0)
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0f - entityYaw))
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-f3))
        val f5 = entityIn.getRollingAmplitude().toFloat() - partialTicks
        var f6: Float = entityIn.getDamage() - partialTicks
        if (f6 < 0.0f) {
            f6 = 0.0f
        }
        if (f5 > 0.0f) {
            matrixStackIn.rotate(Vector3f.XP.rotationDegrees(MathHelper.sin(f5) * f5 * f6 / 10.0f * entityIn.getRollingDirection().toFloat()))
        }
        val j: Int = entityIn.getDisplayTileOffset()
        val blockstate: BlockState = entityIn.getDisplayTile()
        if (blockstate.renderType != BlockRenderType.INVISIBLE) {
            matrixStackIn.push()
            val f4 = 0.75f
            matrixStackIn.scale(0.75f, 0.75f, 0.75f)
            matrixStackIn.translate(-0.5, ((j - 8).toFloat() / 16.0f).toDouble(), 0.5)
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90.0f))
            renderBlockState(entityIn, partialTicks, blockstate, matrixStackIn, bufferIn, packedLightIn)
            matrixStackIn.pop()
        }
        val cameraBlockState = entityIn.cameraBlock
        if (!cameraBlockState.isAir && cameraBlockState.renderType != BlockRenderType.INVISIBLE) {
            matrixStackIn.push()
            val f4 = 0.75f
            matrixStackIn.scale(0.75f, 0.75f, 0.75f)
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(entityIn.cameraRotation.toFloat() * 10f))
            matrixStackIn.translate(-0.5, ((j + 8).toFloat() / 16.0f).toDouble(), 0.5)
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90.0f))
            renderBlockState(entityIn, partialTicks, cameraBlockState, matrixStackIn, bufferIn, packedLightIn)
            matrixStackIn.pop()
        }
        matrixStackIn.scale(-1.0f, -1.0f, 1.0f)
        modelMinecart.setRotationAngles(entityIn, 0.0f, 0.0f, -0.1f, 0.0f, 0.0f)
        val ivertexbuilder = bufferIn.getBuffer(modelMinecart.getRenderType(getEntityTexture(entityIn)))
        modelMinecart.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f)
        matrixStackIn.pop()
    }

}