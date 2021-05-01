package dev.weiland.mods.cameracraft.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
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

    @Invoker("renderDebug")
    void invokeRenderDebug(ActiveRenderInfo p_228446_1_);

    @Invoker("renderHitOutline")
    void invokeRenderHitOutline(MatrixStack p_228429_1_, IVertexBuilder p_228429_2_, Entity p_228429_3_, double p_228429_4_, double p_228429_6_, double p_228429_8_, BlockPos p_228429_10_, BlockState p_228429_11_);

}
