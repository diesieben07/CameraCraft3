package dev.weiland.mods.cameracraft.mixin;

import dev.weiland.mods.cameracraft.viewport.ServerViewportManager;
import net.minecraft.entity.Entity;
import net.minecraft.network.IPacket;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
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
            method = "broadcast(Lnet/minecraft/network/IPacket;)V",
            at = @At("HEAD")
    )
    private void broadcastHook(IPacket<?> packet, CallbackInfo ci) {
        World level = entity.level;
        if (level instanceof ServerWorld) {
            ServerViewportManager.get((ServerWorld) level).sendToTracking(this.entity, packet);
        }
    }

}
