package dev.weiland.mods.cameracraft.mixin;

import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.util.math.Tuple3d;
import net.minecraft.util.math.vector.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(WorldRenderer.class)
public interface WorldRendererAccessor {

    @Accessor("capturedFrustum")
    ClippingHelper cameraCraftGetClippingHelper();

    @Accessor("frustumPos")
    Tuple3d cameraCraftGetFrustumPos();

    @Accessor("captureFrustum")
    boolean getCaptureFrustum();

    @Accessor("captureFrustum")
    void setCaptureFrustum(boolean captureFrustum);

    @Invoker("captureFrustum")
    void invokeCaptureFrustum(Matrix4f p_228419_1_, Matrix4f p_228419_2_, double p_228419_3_, double p_228419_5_, double p_228419_7_, ClippingHelper p_228419_9_);

    @Invoker("renderWorldBounds")
    void invokeRenderWorldBounds(ActiveRenderInfo p_228447_1_);

}
