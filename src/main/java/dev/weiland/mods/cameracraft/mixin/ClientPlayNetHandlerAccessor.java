package dev.weiland.mods.cameracraft.mixin;

import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientPlayNetHandler.class)
public interface ClientPlayNetHandlerAccessor {

    @Accessor("level")
    void setLevel(ClientWorld level);

    @Accessor("levelData")
    void setLevelData(ClientWorld.ClientWorldInfo levelData);

}
