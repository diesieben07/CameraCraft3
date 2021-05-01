package dev.weiland.mods.cameracraft.mixin;

import dev.weiland.mods.cameracraft.viewport.ServerViewportManager;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.server.ChunkHolder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkHolder.class)
public abstract class MixinChunkHolder {

    @Shadow
    @Final
    private ChunkPos pos;

    @Shadow
    @Final
    private ChunkHolder.IPlayerProvider playerProvider;

    @Inject(
            method = "broadcast(Lnet/minecraft/network/IPacket;Z)V",
            at = @At("HEAD")
    )
    private void sendToTracking(IPacket<?> packetIn, boolean boundaryOnly, CallbackInfo ci) {
        if (playerProvider instanceof ChunkManagerAccessor) {
            ServerViewportManager manager = ServerViewportManager.get(((ChunkManagerAccessor) playerProvider).getLevel());
            manager.sendToTracking(this.pos, packetIn, boundaryOnly);
        }
    }

}
