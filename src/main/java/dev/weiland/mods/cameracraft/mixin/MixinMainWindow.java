package dev.weiland.mods.cameracraft.mixin;

import dev.weiland.mods.cameracraft.client.render.SecondaryGameRenderer;
import net.minecraft.client.MainWindow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MainWindow.class)
public abstract class MixinMainWindow {

    @Inject(
            method = "getFramebufferHeight()I",
            at = @At("HEAD"),
            cancellable = true
    )
    private void getFramebufferHeightHook(CallbackInfoReturnable<Integer> ci) {
        SecondaryGameRenderer current = SecondaryGameRenderer.current;
        if (current != null) {
            ci.setReturnValue(current.getImageHeight());
        }
    }

    @Inject(
            method = "getFramebufferWidth()I",
            at = @At("HEAD"),
            cancellable = true
    )
    private void getFramebufferWidthHook(CallbackInfoReturnable<Integer> ci) {
        SecondaryGameRenderer current = SecondaryGameRenderer.current;
        if (current != null) {
            ci.setReturnValue(current.getImageWidth());
        }
    }

}
