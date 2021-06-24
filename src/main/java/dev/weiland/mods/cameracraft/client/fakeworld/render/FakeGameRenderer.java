package dev.weiland.mods.cameracraft.client.fakeworld.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.weiland.mods.cameracraft.mixin.GameRendererAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderTypeBuffers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effects;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;

public class FakeGameRenderer extends GameRenderer {

    private final float width;
    private final float height;
    private final Entity entity;

    public FakeGameRenderer(Minecraft p_i225966_1_, IResourceManager p_i225966_2_, RenderTypeBuffers p_i225966_3_, float width, float height, Entity entity) {
        super(p_i225966_1_, p_i225966_2_, p_i225966_3_);
        this.width = width;
        this.height = height;
        this.entity = entity;

        getMainCamera().eyeHeightOld = getMainCamera().eyeHeight = entity.getEyeHeight();
        ((GameRendererAccessor) this).setRenderHand(false);

        float chunkRenderDistance = 4f;
        ((GameRendererAccessor) this).setRenderDistance(chunkRenderDistance * 16f);
    }

    @Override
    public Matrix4f getProjectionMatrix(ActiveRenderInfo p_228382_1_, float p_228382_2_, boolean p_228382_3_) {
        MatrixStack matrixstack = new MatrixStack();
        matrixstack.last().pose().setIdentity();
        // changed: removed zoom, set FOV fixed, use correct width and height
        matrixstack.last().pose().multiply(Matrix4f.perspective(70d, width / height, 0.05F, this.getRenderDistance() * 4.0F));
        return matrixstack.last().pose();
    }

    @Override
    public void renderLevel(float p_228378_1_, long p_228378_2_, MatrixStack p_228378_4_) {
        Minecraft minecraft = Minecraft.getInstance();
        this.lightTexture.updateLightTexture(p_228378_1_);
        if (minecraft.getCameraEntity() == null) {
            // changed: use our entity
            minecraft.setCameraEntity(this.entity);
        }

        // changed: removed mouse over calculation and block outline

        minecraft.getProfiler().popPush("camera");
        ActiveRenderInfo activerenderinfo = this.getMainCamera();

        // TODO: decide what this value should be
        // changed: use accessor
        ((GameRendererAccessor) this).setRenderDistance((float)(minecraft.options.renderDistance * 16));
        MatrixStack matrixstack = new MatrixStack();
        matrixstack.last().pose().multiply(this.getProjectionMatrix(activerenderinfo, p_228378_1_, true));

        // changed: remove any kind of bob and portal effect

        Matrix4f matrix4f = matrixstack.last().pose();
        this.resetProjectionMatrix(matrix4f);
        // changed: use our entity and remove viewport settings
        activerenderinfo.setup(minecraft.level, (Entity)(minecraft.getCameraEntity() == null ? entity : minecraft.getCameraEntity()), false, false, p_228378_1_);

        // changed: removed camera setup event

        p_228378_4_.mulPose(Vector3f.XP.rotationDegrees(activerenderinfo.getXRot()));
        p_228378_4_.mulPose(Vector3f.YP.rotationDegrees(activerenderinfo.getYRot() + 180.0F));
        // changed: set render block outlines to false
        minecraft.levelRenderer.renderLevel(p_228378_4_, p_228378_1_, p_228378_2_, false, activerenderinfo, this, this.lightTexture, matrix4f);

        // changed: removed render last event

        // changed: removed hand rendering
    }
}
