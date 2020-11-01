package dev.weiland.mods.cameracraft.mixin;

import dev.weiland.mods.cameracraft.viewport.ServerViewportManager;
import net.minecraft.entity.Entity;
import net.minecraft.network.IPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.world.server.ChunkManager$EntityTracker")
public abstract class MixinEntityTracker {

    @Shadow
    @Final
    private Entity entity;

    @Inject(
            method = "sendToAllTracking(Lnet/minecraft/network/IPacket;)V",
            at = @At("HEAD")
    )
    private void sendToAllTrackingHook(IPacket<?> packet, CallbackInfo ci) {
        ServerViewportManager.get().sendToTracking(this.entity, packet);
    }

}
