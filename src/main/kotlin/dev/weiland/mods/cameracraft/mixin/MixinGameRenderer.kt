package dev.weiland.mods.cameracraft.mixin

import dev.weiland.mods.cameracraft.client.render.SecondaryGameRenderer
import net.minecraft.client.renderer.ActiveRenderInfo
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.util.math.vector.Matrix4f
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import org.spongepowered.asm.mixin.injection.callback.LocalCapture

@Mixin(GameRenderer::class)
abstract class MixinGameRenderer {

    @Inject(
        at = [At("HEAD")],
        method = ["Lnet/minecraft/client/renderer/GameRenderer;getProjectionMatrix(Lnet/minecraft/client/renderer/ActiveRenderInfo;FZ)Lnet/minecraft/util/math/vector/Matrix4f;"],
        cancellable = true,
    )
    private fun getProjectionMatrix(
        activeRenderInfoIn: ActiveRenderInfo?,
        partialTicks: Float,
        useFovSetting: Boolean,
        ci: CallbackInfoReturnable<Matrix4f>
    ) {
        if (SecondaryGameRenderer.inFakeRender) {
            @Suppress("CAST_NEVER_SUCCEEDS")
            ci.returnValue = SecondaryGameRenderer.fakeRenderProjectionMatrix(this as GameRenderer, activeRenderInfoIn, partialTicks, useFovSetting)
            ci.cancel()
        }
    }

}