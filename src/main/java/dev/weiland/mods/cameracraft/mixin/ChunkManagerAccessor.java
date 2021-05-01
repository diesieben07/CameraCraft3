package dev.weiland.mods.cameracraft.mixin;

import net.minecraft.world.server.ChunkManager;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ChunkManager.class)
public interface ChunkManagerAccessor {

    @Accessor("level")
    ServerWorld getLevel();

}
