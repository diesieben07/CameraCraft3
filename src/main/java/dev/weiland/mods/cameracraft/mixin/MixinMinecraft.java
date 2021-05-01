package dev.weiland.mods.cameracraft.mixin;

import dev.weiland.mods.cameracraft.client.fakeworld.render.SecondaryGameRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Framebuffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {

    @Inject(
            method = "getMainRenderTarget()Lnet/minecraft/client/shader/Framebuffer;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void getMainRenderTargetHook(CallbackInfoReturnable<Framebuffer> ci) {
        SecondaryGameRenderer currentRender = SecondaryGameRenderer.current;
        if (currentRender != null) {
            ci.setReturnValue(currentRender.getFrameBuffer());
        }
    }

}
