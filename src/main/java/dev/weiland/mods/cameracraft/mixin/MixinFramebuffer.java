package dev.weiland.mods.cameracraft.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.weiland.mods.cameracraft.client.fakeworld.render.SecondaryGameRenderer;
import net.minecraft.client.shader.Framebuffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(Framebuffer.class)
abstract class MixinFramebuffer {

    @Redirect(
        method = "_bindWrite",
        at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/GlStateManager;_viewport(IIII)V")
    )
    private void hookViewport(int x, int y, int w, int h) {
        SecondaryGameRenderer current = SecondaryGameRenderer.current;
        if (current != null) {
            GlStateManager._viewport(x, y, current.getImageWidth(), current.getImageHeight());
        } else {
            GlStateManager._viewport(x, y, w, h);
        }
    }

}