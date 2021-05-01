package dev.weiland.mods.cameracraft.client.fakeworld.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.vertex.MatrixApplyingVertexBuilder;
import com.mojang.blaze3d.vertex.VertexBuilderUtils;
import dev.weiland.mods.cameracraft.mixin.WorldRendererAccessor;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.minecraft.block.BlockState;
import net.minecraft.client.AbstractOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.settings.CloudOption;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.profiler.IProfiler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Util;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.SortedSet;

@ParametersAreNonnullByDefault
public class FakeWorldRenderer extends WorldRenderer {

    private static final Logger LOGGER = LogManager.getLogger();

    private final Framebuffer mainFramebuffer;

    public FakeWorldRenderer(Minecraft mc, RenderTypeBuffers renderBuffers, Framebuffer mainFramebuffer) {
        super(mc, renderBuffers);
        this.mainFramebuffer = mainFramebuffer;
    }

    @Override
    public void renderLevel(MatrixStack p_228426_1_, float p_228426_2_, long p_228426_3_, boolean p_228426_5_, ActiveRenderInfo p_228426_6_, GameRenderer p_228426_7_, LightTexture p_228426_8_, Matrix4f p_228426_9_) {
        TileEntityRendererDispatcher.instance.prepare(this.level, this.minecraft.getTextureManager(), this.minecraft.font, p_228426_6_, this.minecraft.hitResult);
        this.entityRenderDispatcher.prepare(this.level, p_228426_6_, this.minecraft.crosshairPickEntity);
        IProfiler iprofiler = this.level.getProfiler();
        iprofiler.popPush("light_updates");
        this.minecraft.level.getChunkSource().getLightEngine().runUpdates(Integer.MAX_VALUE, true, true);
        Vector3d vector3d = p_228426_6_.getPosition();
        double d0 = vector3d.x();
        double d1 = vector3d.y();
        double d2 = vector3d.z();
        Matrix4f matrix4f = p_228426_1_.last().pose();
        iprofiler.popPush("culling");
        // changed: use accessor
        ClippingHelper capturedFrustum = ((WorldRendererAccessor) this).cameraCraftGetClippingHelper();
        boolean flag = capturedFrustum != null;
        ClippingHelper clippinghelper;
        if (flag) {
            // changed: use accessor
            clippinghelper = capturedFrustum;
            Tuple3d frustumPos = ((WorldRendererAccessor) this).cameraCraftGetFrustumPos();
            clippinghelper.prepare(frustumPos.x, frustumPos.y, frustumPos.z);
        } else {
            clippinghelper = new ClippingHelper(matrix4f, p_228426_9_);
            clippinghelper.prepare(d0, d1, d2);
        }

        this.minecraft.getProfiler().popPush("captureFrustum");
        // changed: use accessor
        if (((WorldRendererAccessor) this).getCaptureFrustum()) {
            // changed: user accessor
            ((WorldRendererAccessor) this).invokeCaptureFrustum(matrix4f, p_228426_9_, vector3d.x, vector3d.y, vector3d.z, flag ? new ClippingHelper(matrix4f, p_228426_9_) : clippinghelper);
            // changed: use accessor
            ((WorldRendererAccessor) this).setCaptureFrustum(false);
        }

        iprofiler.popPush("clear");
        FogRenderer.setupColor(p_228426_6_, p_228426_2_, this.minecraft.level, this.minecraft.options.renderDistance, p_228426_7_.getDarkenWorldAmount(p_228426_2_));
        RenderSystem.clear(16640, Minecraft.ON_OSX);
        float f = p_228426_7_.getRenderDistance();
        boolean flag1 = this.minecraft.level.effects().isFoggyAt(MathHelper.floor(d0), MathHelper.floor(d1)) || this.minecraft.gui.getBossOverlay().shouldCreateWorldFog();
        if (this.minecraft.options.renderDistance >= 4) {
            FogRenderer.setupFog(p_228426_6_, FogRenderer.FogType.FOG_SKY, f, flag1, p_228426_2_);
            iprofiler.popPush("sky");
            this.renderSky(p_228426_1_, p_228426_2_);
        }

        iprofiler.popPush("fog");
        FogRenderer.setupFog(p_228426_6_, FogRenderer.FogType.FOG_TERRAIN, Math.max(f - 16.0F, 32.0F), flag1, p_228426_2_);
        iprofiler.popPush("terrain_setup");
        this.setupRender(p_228426_6_, clippinghelper, flag, this.frameId++, this.minecraft.player.isSpectator());
        iprofiler.popPush("updatechunks");
        int i = 30;
        int j = this.minecraft.options.framerateLimit;
        long k = 33333333L;
        long l;
        if ((double)j == AbstractOption.FRAMERATE_LIMIT.getMaxValue()) {
            l = 0L;
        } else {
            l = (long)(1000000000 / j);
        }

        long i1 = Util.getNanos() - p_228426_3_;
        long j1 = this.frameTimes.registerValueAndGetMean(i1);
        long k1 = j1 * 3L / 2L;
        long l1 = MathHelper.clamp(k1, l, 33333333L);
        this.compileChunksUntil(p_228426_3_ + l1);
        iprofiler.popPush("terrain");
        this.renderChunkLayer(RenderType.solid(), p_228426_1_, d0, d1, d2);
        this.minecraft.getModelManager().getAtlas(AtlasTexture.LOCATION_BLOCKS).setBlurMipmap(false, this.minecraft.options.mipmapLevels > 0); // FORGE: fix flickering leaves when mods mess up the blurMipmap settings
        this.renderChunkLayer(RenderType.cutoutMipped(), p_228426_1_, d0, d1, d2);
        this.minecraft.getModelManager().getAtlas(AtlasTexture.LOCATION_BLOCKS).restoreLastBlurMipmap();
        this.renderChunkLayer(RenderType.cutout(), p_228426_1_, d0, d1, d2);
        if (this.level.effects().constantAmbientLight()) {
            RenderHelper.setupNetherLevel(p_228426_1_.last().pose());
        } else {
            RenderHelper.setupLevel(p_228426_1_.last().pose());
        }

        iprofiler.popPush("entities");
        this.renderedEntities = 0;
        this.culledEntities = 0;
        if (this.itemEntityTarget != null) {
            this.itemEntityTarget.clear(Minecraft.ON_OSX);
            // changed: Use our framebuffer
            this.itemEntityTarget.copyDepthFrom(this.mainFramebuffer);
            this.mainFramebuffer.bindWrite(false);
        }

        if (this.weatherTarget != null) {
            this.weatherTarget.clear(Minecraft.ON_OSX);
        }

        if (this.shouldShowEntityOutlines()) {
            this.entityTarget.clear(Minecraft.ON_OSX);
            // changed: use our framebuffer
            this.mainFramebuffer.bindWrite(false);
        }

        boolean flag2 = false;
        IRenderTypeBuffer.Impl irendertypebuffer$impl = this.renderBuffers.bufferSource();

        for(Entity entity : this.level.entitiesForRendering()) {
            // changed: fixed so local player renders in camera view
            if ((entityRenderDispatcher.shouldRender(entity, clippinghelper, d0, d1, d2) || entity.hasIndirectPassenger(this.minecraft.player)) && entity != p_228426_6_.getEntity()) { //FORGE: render local player entity when it is not the renderViewEntity
                ++this.renderedEntities;
                if (entity.tickCount == 0) {
                    entity.xOld = entity.getX();
                    entity.yOld = entity.getY();
                    entity.zOld = entity.getZ();
                }

                IRenderTypeBuffer irendertypebuffer;
                if (this.shouldShowEntityOutlines() && this.minecraft.shouldEntityAppearGlowing(entity)) {
                    flag2 = true;
                    OutlineLayerBuffer outlinelayerbuffer = this.renderBuffers.outlineBufferSource();
                    irendertypebuffer = outlinelayerbuffer;
                    int i2 = entity.getTeamColor();
                    int j2 = 255;
                    int k2 = i2 >> 16 & 255;
                    int l2 = i2 >> 8 & 255;
                    int i3 = i2 & 255;
                    outlinelayerbuffer.setColor(k2, l2, i3, 255);
                } else {
                    irendertypebuffer = irendertypebuffer$impl;
                }

                this.renderEntity(entity, d0, d1, d2, p_228426_2_, p_228426_1_, irendertypebuffer);
            }
        }

        this.checkPoseStack(p_228426_1_);
        irendertypebuffer$impl.endBatch(RenderType.entitySolid(AtlasTexture.LOCATION_BLOCKS));
        irendertypebuffer$impl.endBatch(RenderType.entityCutout(AtlasTexture.LOCATION_BLOCKS));
        irendertypebuffer$impl.endBatch(RenderType.entityCutoutNoCull(AtlasTexture.LOCATION_BLOCKS));
        irendertypebuffer$impl.endBatch(RenderType.entitySmoothCutout(AtlasTexture.LOCATION_BLOCKS));
        iprofiler.popPush("blockentities");

        for(WorldRenderer.LocalRenderInformationContainer worldrenderer$localrenderinformationcontainer : this.renderChunks) {
            List<TileEntity> list = worldrenderer$localrenderinformationcontainer.chunk.getCompiledChunk().getRenderableBlockEntities();
            if (!list.isEmpty()) {
                for(TileEntity tileentity1 : list) {
                    if(!clippinghelper.isVisible(tileentity1.getRenderBoundingBox())) continue;
                    BlockPos blockpos3 = tileentity1.getBlockPos();
                    IRenderTypeBuffer irendertypebuffer1 = irendertypebuffer$impl;
                    p_228426_1_.pushPose();
                    p_228426_1_.translate((double)blockpos3.getX() - d0, (double)blockpos3.getY() - d1, (double)blockpos3.getZ() - d2);
                    SortedSet<DestroyBlockProgress> sortedset = this.destructionProgress.get(blockpos3.asLong());
                    if (sortedset != null && !sortedset.isEmpty()) {
                        int j3 = sortedset.last().getProgress();
                        if (j3 >= 0) {
                            MatrixStack.Entry matrixstack$entry = p_228426_1_.last();
                            IVertexBuilder ivertexbuilder = new MatrixApplyingVertexBuilder(this.renderBuffers.crumblingBufferSource().getBuffer(ModelBakery.DESTROY_TYPES.get(j3)), matrixstack$entry.pose(), matrixstack$entry.normal());
                            irendertypebuffer1 = (p_230014_2_) -> {
                                IVertexBuilder ivertexbuilder3 = irendertypebuffer$impl.getBuffer(p_230014_2_);
                                return p_230014_2_.affectsCrumbling() ? VertexBuilderUtils.create(ivertexbuilder, ivertexbuilder3) : ivertexbuilder3;
                            };
                        }
                    }

                    TileEntityRendererDispatcher.instance.render(tileentity1, p_228426_2_, p_228426_1_, irendertypebuffer1);
                    p_228426_1_.popPose();
                }
            }
        }

        synchronized(this.globalBlockEntities) {
            for(TileEntity tileentity : this.globalBlockEntities) {
                if(!clippinghelper.isVisible(tileentity.getRenderBoundingBox())) continue;
                BlockPos blockpos2 = tileentity.getBlockPos();
                p_228426_1_.pushPose();
                p_228426_1_.translate((double)blockpos2.getX() - d0, (double)blockpos2.getY() - d1, (double)blockpos2.getZ() - d2);
                TileEntityRendererDispatcher.instance.render(tileentity, p_228426_2_, p_228426_1_, irendertypebuffer$impl);
                p_228426_1_.popPose();
            }
        }

        this.checkPoseStack(p_228426_1_);
        irendertypebuffer$impl.endBatch(RenderType.solid());
        irendertypebuffer$impl.endBatch(Atlases.solidBlockSheet());
        irendertypebuffer$impl.endBatch(Atlases.cutoutBlockSheet());
        irendertypebuffer$impl.endBatch(Atlases.bedSheet());
        irendertypebuffer$impl.endBatch(Atlases.shulkerBoxSheet());
        irendertypebuffer$impl.endBatch(Atlases.signSheet());
        irendertypebuffer$impl.endBatch(Atlases.chestSheet());
        this.renderBuffers.outlineBufferSource().endOutlineBatch();
        if (flag2) {
            this.entityEffect.process(p_228426_2_);
            // changed: use our framebuffer
            mainFramebuffer.bindWrite(false);
        }

        iprofiler.popPush("destroyProgress");

        for(Long2ObjectMap.Entry<SortedSet<DestroyBlockProgress>> entry : this.destructionProgress.long2ObjectEntrySet()) {
            BlockPos blockpos1 = BlockPos.of(entry.getLongKey());
            double d3 = (double)blockpos1.getX() - d0;
            double d4 = (double)blockpos1.getY() - d1;
            double d5 = (double)blockpos1.getZ() - d2;
            if (!(d3 * d3 + d4 * d4 + d5 * d5 > 1024.0D)) {
                SortedSet<DestroyBlockProgress> sortedset1 = entry.getValue();
                if (sortedset1 != null && !sortedset1.isEmpty()) {
                    int k3 = sortedset1.last().getProgress();
                    p_228426_1_.pushPose();
                    p_228426_1_.translate((double)blockpos1.getX() - d0, (double)blockpos1.getY() - d1, (double)blockpos1.getZ() - d2);
                    MatrixStack.Entry matrixstack$entry1 = p_228426_1_.last();
                    IVertexBuilder ivertexbuilder1 = new MatrixApplyingVertexBuilder(this.renderBuffers.crumblingBufferSource().getBuffer(ModelBakery.DESTROY_TYPES.get(k3)), matrixstack$entry1.pose(), matrixstack$entry1.normal());
                    this.minecraft.getBlockRenderer().renderBreakingTexture(this.level.getBlockState(blockpos1), blockpos1, this.level, p_228426_1_, ivertexbuilder1);
                    p_228426_1_.popPose();
                }
            }
        }

        this.checkPoseStack(p_228426_1_);
        // changed: disable outline render and debug render
//        RayTraceResult raytraceresult = this.minecraft.hitResult;
//        if (p_228426_5_ && raytraceresult != null && raytraceresult.getType() == RayTraceResult.Type.BLOCK) {
//            iprofiler.popPush("outline");
//            BlockPos blockpos = ((BlockRayTraceResult)raytraceresult).getBlockPos();
//            BlockState blockstate = this.level.getBlockState(blockpos);
//            if (!net.minecraftforge.client.ForgeHooksClient.onDrawBlockHighlight(this, p_228426_6_, raytraceresult, p_228426_2_, p_228426_1_, irendertypebuffer$impl))
//                if (!blockstate.isAir(this.level, blockpos) && this.level.getWorldBorder().isWithinBounds(blockpos)) {
//                    IVertexBuilder ivertexbuilder2 = irendertypebuffer$impl.getBuffer(RenderType.lines());
//                    // changed: use accessor
//                    ((WorldRendererAccessor) this).invokeRenderHitOutline(p_228426_1_, ivertexbuilder2, p_228426_6_.getEntity(), d0, d1, d2, blockpos, blockstate);
//                }
//        } else if (raytraceresult != null && raytraceresult.getType() == RayTraceResult.Type.ENTITY) {
//            net.minecraftforge.client.ForgeHooksClient.onDrawBlockHighlight(this, p_228426_6_, raytraceresult, p_228426_2_, p_228426_1_, irendertypebuffer$impl);
//        }
//
//        RenderSystem.pushMatrix();
//        RenderSystem.multMatrix(p_228426_1_.last().pose());
//        this.minecraft.debugRenderer.render(p_228426_1_, irendertypebuffer$impl, d0, d1, d2);
//        RenderSystem.popMatrix();
        irendertypebuffer$impl.endBatch(Atlases.translucentCullBlockSheet());
        irendertypebuffer$impl.endBatch(Atlases.bannerSheet());
        irendertypebuffer$impl.endBatch(Atlases.shieldSheet());
        irendertypebuffer$impl.endBatch(RenderType.armorGlint());
        irendertypebuffer$impl.endBatch(RenderType.armorEntityGlint());
        irendertypebuffer$impl.endBatch(RenderType.glint());
        irendertypebuffer$impl.endBatch(RenderType.glintDirect());
        irendertypebuffer$impl.endBatch(RenderType.glintTranslucent());
        irendertypebuffer$impl.endBatch(RenderType.entityGlint());
        irendertypebuffer$impl.endBatch(RenderType.entityGlintDirect());
        irendertypebuffer$impl.endBatch(RenderType.waterMask());
        this.renderBuffers.crumblingBufferSource().endBatch();
        if (this.transparencyChain != null) {
            irendertypebuffer$impl.endBatch(RenderType.lines());
            irendertypebuffer$impl.endBatch();
            this.translucentTarget.clear(Minecraft.ON_OSX);
            this.translucentTarget.copyDepthFrom(this.minecraft.getMainRenderTarget());
            iprofiler.popPush("translucent");
            this.renderChunkLayer(RenderType.translucent(), p_228426_1_, d0, d1, d2);
            iprofiler.popPush("string");
            this.renderChunkLayer(RenderType.tripwire(), p_228426_1_, d0, d1, d2);
            this.particlesTarget.clear(Minecraft.ON_OSX);
            this.particlesTarget.copyDepthFrom(this.minecraft.getMainRenderTarget());
            // TODO: particles
//            RenderState.PARTICLES_TARGET.setupRenderState();
//            iprofiler.popPush("particles");
//            this.minecraft.particleEngine.renderParticles(p_228426_1_, irendertypebuffer$impl, p_228426_8_, p_228426_6_, p_228426_2_, clippinghelper);
//            RenderState.PARTICLES_TARGET.clearRenderState();
        } else {
            iprofiler.popPush("translucent");
            this.renderChunkLayer(RenderType.translucent(), p_228426_1_, d0, d1, d2);
            irendertypebuffer$impl.endBatch(RenderType.lines());
            irendertypebuffer$impl.endBatch();
            iprofiler.popPush("string");
            this.renderChunkLayer(RenderType.tripwire(), p_228426_1_, d0, d1, d2);
            // TODO: particles
//            iprofiler.popPush("particles");
//            this.minecraft.particleEngine.renderParticles(p_228426_1_, irendertypebuffer$impl, p_228426_8_, p_228426_6_, p_228426_2_, clippinghelper);
        }

        RenderSystem.pushMatrix();
        RenderSystem.multMatrix(p_228426_1_.last().pose());
        if (this.minecraft.options.getCloudsType() != CloudOption.OFF) {
            if (this.transparencyChain != null) {
                this.cloudsTarget.clear(Minecraft.ON_OSX);
                RenderState.CLOUDS_TARGET.setupRenderState();
                iprofiler.popPush("clouds");
                this.renderClouds(p_228426_1_, p_228426_2_, d0, d1, d2);
                RenderState.CLOUDS_TARGET.clearRenderState();
            } else {
                iprofiler.popPush("clouds");
                this.renderClouds(p_228426_1_, p_228426_2_, d0, d1, d2);
            }
        }

        if (this.transparencyChain != null) {
            RenderState.WEATHER_TARGET.setupRenderState();
            iprofiler.popPush("weather");
            this.renderSnowAndRain(p_228426_8_, p_228426_2_, d0, d1, d2);
            // changed: use accessor
            ((WorldRendererAccessor) this).invokeRenderWorldBounds(p_228426_6_);
            RenderState.WEATHER_TARGET.clearRenderState();
            this.transparencyChain.process(p_228426_2_);
            // changed: use our framebuffer
            mainFramebuffer.bindWrite(false);
        } else {
            RenderSystem.depthMask(false);
            iprofiler.popPush("weather");
            this.renderSnowAndRain(p_228426_8_, p_228426_2_, d0, d1, d2);
            // changed: use accessor
            ((WorldRendererAccessor) this).invokeRenderWorldBounds(p_228426_6_);
            RenderSystem.depthMask(true);
        }

        // changed: remove debug render
        RenderSystem.shadeModel(7424);
        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
        RenderSystem.popMatrix();
        FogRenderer.setupNoFog();
    }
}
