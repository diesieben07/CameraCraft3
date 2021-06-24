package dev.weiland.mods.cameracraft.mixin;

import net.minecraft.world.TrackedEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets = "net.minecraft.world.server.ChunkManager$EntityTracker")
public interface ChunkManagerEntityTrackerAccessor {

    @Accessor("serverEntity")
    TrackedEntity getServerEntity();

}
