package dev.weiland.mods.cameracraft.mixin

import com.mojang.blaze3d.platform.GlStateManager
import dev.weiland.mods.cameracraft.client.render.SecondaryGameRenderer
import net.minecraft.client.Minecraft
import net.minecraft.client.shader.Framebuffer
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Redirect


@Mixin(Framebuffer::class)
internal class MixinFramebuffer {

    @Redirect(
        method = ["bindFramebufferRaw"],
        at = At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/GlStateManager;viewport(IIII)V")
    )
    private fun hookViewport(x: Int, y: Int, w: Int, h: Int) {
        if (SecondaryGameRenderer.inFakeRender) {
            // during fake render, force every viewport call to be at the correct size
            GlStateManager.viewport(x, y, Minecraft.getInstance().framebuffer.framebufferWidth, Minecraft.getInstance().framebuffer.framebufferHeight)
        } else {
            GlStateManager.viewport(x, y, w, h)
        }
    }

}