package dev.weiland.mods.cameracraft.mixin

import dev.weiland.mods.cameracraft.client.render.SecondaryGameRenderer
import net.minecraft.client.GameConfiguration
import net.minecraft.client.Minecraft
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(Minecraft::class)
abstract class MixinMinecraft {

    @Inject(
        method = ["runGameLoop(Z)V"],
        at = [
//            At(
//                "HEAD"
//            )
            At(
                "INVOKE_STRING",
                target = "endStartSection(Ljava/lang/String;)V",
                args = ["ldc=updateDisplay"],
                shift = At.Shift.AFTER
            )
        ]
    )
    fun runGameLoop(renderWorldIn: Boolean, ci: CallbackInfo) {
//        SecondaryGameRenderer.doRender()
    }

}