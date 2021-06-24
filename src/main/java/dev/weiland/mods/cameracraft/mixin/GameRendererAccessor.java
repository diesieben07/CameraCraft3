package dev.weiland.mods.cameracraft.mixin;

import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GameRenderer.class)
public interface GameRendererAccessor {

    @Accessor("renderHand")
    void setRenderHand(boolean renderHand);

    @Accessor("renderDistance")
    void setRenderDistance(float renderDistance);

}
