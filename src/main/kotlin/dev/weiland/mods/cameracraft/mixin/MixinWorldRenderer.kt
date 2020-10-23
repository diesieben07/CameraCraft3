package dev.weiland.mods.cameracraft.mixin

import com.mojang.blaze3d.matrix.MatrixStack
import dev.weiland.mods.cameracraft.client.render.SecondaryGameRenderer
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.*
import net.minecraft.client.renderer.entity.EntityRendererManager
import net.minecraft.entity.Entity
import net.minecraft.util.math.vector.Matrix4f
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(WorldRenderer::class)
abstract class MixinWorldRenderer {

    @Shadow
    private lateinit var renderManager: EntityRendererManager

    @Shadow
    private lateinit var renderTypeTextures: RenderTypeBuffers

    @Shadow
    private fun renderEntity(
        entityIn: Entity, camX: Double, camY: Double, camZ: Double, partialTicks: Float, matrixStackIn: MatrixStack?, bufferIn: IRenderTypeBuffer
    ) {}

    @Inject(
        method = ["Lnet/minecraft/client/renderer/WorldRenderer;updateCameraAndRender(Lcom/mojang/blaze3d/matrix/MatrixStack;FJZLnet/minecraft/client/renderer/ActiveRenderInfo;Lnet/minecraft/client/renderer/GameRenderer;Lnet/minecraft/client/renderer/LightTexture;Lnet/minecraft/util/math/vector/Matrix4f;)V"],
        at = [
            At(
                "INVOKE",
                target = "Lnet/minecraft/client/world/ClientWorld;getAllEntities()Ljava/lang/Iterable;"
            )
        ],
        allow = 1,
        require = 1
    )
    private fun onEntityRendering(
        matrixStackIn: MatrixStack?, partialTicks: Float, finishTimeNano: Long, drawBlockOutline: Boolean, activeRenderInfoIn: ActiveRenderInfo?,
        gameRendererIn: GameRenderer?, lightmapIn: LightTexture?, projectionIn: Matrix4f?,
        ci: CallbackInfo
    ) {
        if (SecondaryGameRenderer.inFakeRender) {
            val mc = Minecraft.getInstance()
            val entity = mc.player!!

            val vector3d = activeRenderInfoIn!!.projectedView
            val d0 = vector3d.getX()
            val d1 = vector3d.getY()
            val d2 = vector3d.getZ()

            if (entity.ticksExisted == 0) {
                entity.lastTickPosX = entity.getPosX()
                entity.lastTickPosY = entity.getPosY()
                entity.lastTickPosZ = entity.getPosZ()
            }
            val irendertypebuffer: IRenderTypeBuffer = renderTypeTextures.bufferSource
            this.renderEntity(entity, d0, d1, d2, partialTicks, matrixStackIn, irendertypebuffer)
        }
    }

}