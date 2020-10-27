package dev.weiland.mods.cameracraft.client.render

import com.google.gson.JsonSyntaxException
import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.IVertexBuilder
import com.mojang.blaze3d.vertex.MatrixApplyingVertexBuilder
import com.mojang.blaze3d.vertex.VertexBuilderUtils
import net.minecraft.client.AbstractOption
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.*
import net.minecraft.client.renderer.culling.ClippingHelper
import net.minecraft.client.renderer.model.ModelBakery
import net.minecraft.client.renderer.texture.AtlasTexture
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher
import net.minecraft.client.settings.CloudOption
import net.minecraft.client.settings.GraphicsFanciness
import net.minecraft.client.shader.Framebuffer
import net.minecraft.client.shader.ShaderGroup
import net.minecraft.crash.CrashReport
import net.minecraft.util.ResourceLocation
import net.minecraft.util.Util
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.vector.Matrix4f
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent
import org.apache.logging.log4j.LogManager
import java.io.IOException

internal class FakeWorldRenderer(
        mc: Minecraft,
        rainTimeBuffersIn: RenderTypeBuffers,
        private val mainFramebuffer: Framebuffer
) : WorldRenderer(mc, rainTimeBuffersIn) {

    private companion object {
        private val LOGGER = LogManager.getLogger()
    }

    override fun updateCameraAndRender(matrixStackIn: MatrixStack, partialTicks: Float, finishTimeNano: Long, drawBlockOutline: Boolean, activeRenderInfoIn: ActiveRenderInfo, gameRendererIn: GameRenderer, lightmapIn: LightTexture?, projectionIn: Matrix4f?) {
        TileEntityRendererDispatcher.instance.prepare(world, this.mc.getTextureManager(), this.mc.fontRenderer, activeRenderInfoIn, this.mc.objectMouseOver)
        renderManager.cacheActiveRenderInfo(world, activeRenderInfoIn, this.mc.pointedEntity)
        val iprofiler = world.profiler
        iprofiler.endStartSection("light_updates")
        this.mc.world!!.chunkProvider.lightManager.tick(Int.MAX_VALUE, true, true)
        val vector3d = activeRenderInfoIn.projectedView
        val d0 = vector3d.getX()
        val d1 = vector3d.getY()
        val d2 = vector3d.getZ()
        val matrix4f = matrixStackIn.last.matrix
        iprofiler.endStartSection("culling")
        val flag = false //debugFixedClippingHelper != null
        val clippinghelper: ClippingHelper?
//        if (flag) {
//            clippinghelper = debugFixedClippingHelper
//            clippinghelper!!.setCameraPosition(debugTerrainFrustumPosition.x, debugTerrainFrustumPosition.y, debugTerrainFrustumPosition.z)
//        } else {
            clippinghelper = ClippingHelper(matrix4f, projectionIn)
            clippinghelper.setCameraPosition(d0, d1, d2)
//        }
        this.mc.profiler.endStartSection("captureFrustum")
//        if (debugFixTerrainFrustum) {
//            captureFrustum(matrix4f, projectionIn, vector3d.x, vector3d.y, vector3d.z, if (flag) ClippingHelper(matrix4f, projectionIn) else clippinghelper)
//            debugFixTerrainFrustum = false
//        }
        iprofiler.endStartSection("clear")
        FogRenderer.updateFogColor(activeRenderInfoIn, partialTicks, this.mc.world, this.mc.gameSettings.renderDistanceChunks, gameRendererIn.getBossColorModifier(partialTicks))
        RenderSystem.clear(16640, Minecraft.IS_RUNNING_ON_MAC)
        val f = gameRendererIn.farPlaneDistance
        val flag1 = this.mc.world!!.func_239132_a_().func_230493_a_(MathHelper.floor(d0), MathHelper.floor(d1)) || this.mc.ingameGUI.bossOverlay.shouldCreateFog()
        if (this.mc.gameSettings.renderDistanceChunks >= 4) {
            FogRenderer.setupFog(activeRenderInfoIn, FogRenderer.FogType.FOG_SKY, f, flag1, partialTicks)
            iprofiler.endStartSection("sky")
            this.renderSky(matrixStackIn, partialTicks)
        }
        iprofiler.endStartSection("fog")
        FogRenderer.setupFog(activeRenderInfoIn, FogRenderer.FogType.FOG_TERRAIN, Math.max(f - 16.0f, 32.0f), flag1, partialTicks)
        iprofiler.endStartSection("terrain_setup")
        setupTerrain(activeRenderInfoIn, clippinghelper, flag, frameId++, this.mc.player!!.isSpectator)
        iprofiler.endStartSection("updatechunks")
        val i = 30
        val j = this.mc.gameSettings.framerateLimit
        val k = 33333333L
        val l: Long
        l = if (j.toDouble() == AbstractOption.FRAMERATE_LIMIT.maxValue) {
            0L
        } else {
            (1000000000 / j).toLong()
        }
        val i1 = Util.nanoTime() - finishTimeNano
        val j1 = renderTimeManager.nextValue(i1)
        val k1 = j1 * 3L / 2L
        val l1 = MathHelper.clamp(k1, l, 33333333L)
        updateChunks(finishTimeNano + l1)
        iprofiler.endStartSection("terrain")
        renderBlockLayer(RenderType.getSolid(), matrixStackIn, d0, d1, d2)
        this.mc.modelManager.getAtlasTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, this.mc.gameSettings.mipmapLevels > 0) // FORGE: fix flickering leaves when mods mess up the blurMipmap settings
        renderBlockLayer(RenderType.getCutoutMipped(), matrixStackIn, d0, d1, d2)
        this.mc.modelManager.getAtlasTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap()
        renderBlockLayer(RenderType.getCutout(), matrixStackIn, d0, d1, d2)
        if (world.func_239132_a_().func_239217_c_()) {
            RenderHelper.setupDiffuseGuiLighting(matrixStackIn.last.matrix)
        } else {
            RenderHelper.setupLevelDiffuseLighting(matrixStackIn.last.matrix)
        }
        iprofiler.endStartSection("entities")
        countEntitiesRendered = 0
        countEntitiesHidden = 0
        if (field_239223_G_ != null) {
            field_239223_G_!!.framebufferClear(Minecraft.IS_RUNNING_ON_MAC)
            // changed to our framebuffer
            field_239223_G_!!.func_237506_a_(this.mainFramebuffer)
            this.mainFramebuffer.bindFramebuffer(false)
        }
        if (field_239225_I_ != null) {
            field_239225_I_!!.framebufferClear(Minecraft.IS_RUNNING_ON_MAC)
        }
        if (this.isRenderEntityOutlines) {
            entityOutlineFramebuffer!!.framebufferClear(Minecraft.IS_RUNNING_ON_MAC)
            // changed to our framebuffer
            mainFramebuffer.bindFramebuffer(false)
        }
        var flag2 = false
        val `irendertypebuffer$impl` = renderTypeTextures.bufferSource
        for (entity in world.allEntities) {
            // changed: fixed so local player renders in camera view
            if (
                    (renderManager.shouldRender(entity, clippinghelper, d0, d1, d2) || entity.isRidingOrBeingRiddenBy(this.mc.player))
                    && (entity !== activeRenderInfoIn.renderViewEntity)
            ) {
                ++countEntitiesRendered
                if (entity.ticksExisted == 0) {
                    entity.lastTickPosX = entity.posX
                    entity.lastTickPosY = entity.posY
                    entity.lastTickPosZ = entity.posZ
                }
                var irendertypebuffer: IRenderTypeBuffer?
                if (this.isRenderEntityOutlines && this.mc.isEntityGlowing(entity)) {
                    flag2 = true
                    val outlinelayerbuffer = renderTypeTextures.outlineBufferSource
                    irendertypebuffer = outlinelayerbuffer
                    val i2 = entity.teamColor
                    val j2 = 255
                    val k2 = i2 shr 16 and 255
                    val l2 = i2 shr 8 and 255
                    val i3 = i2 and 255
                    outlinelayerbuffer.setColor(k2, l2, i3, 255)
                } else {
                    irendertypebuffer = `irendertypebuffer$impl`
                }
                renderEntity(entity, d0, d1, d2, partialTicks, matrixStackIn, irendertypebuffer)
            }
        }
        checkMatrixStack(matrixStackIn)
        `irendertypebuffer$impl`.finish(RenderType.getEntitySolid(AtlasTexture.LOCATION_BLOCKS_TEXTURE))
        `irendertypebuffer$impl`.finish(RenderType.getEntityCutout(AtlasTexture.LOCATION_BLOCKS_TEXTURE))
        `irendertypebuffer$impl`.finish(RenderType.getEntityCutoutNoCull(AtlasTexture.LOCATION_BLOCKS_TEXTURE))
        `irendertypebuffer$impl`.finish(RenderType.getEntitySmoothCutout(AtlasTexture.LOCATION_BLOCKS_TEXTURE))
        iprofiler.endStartSection("blockentities")
        for (`worldrenderer$localrenderinformationcontainer` in renderInfos) {
            val list = `worldrenderer$localrenderinformationcontainer`.renderChunk.getCompiledChunk().tileEntities
            if (!list.isEmpty()) {
                for (tileentity1 in list) {
                    if (!clippinghelper.isBoundingBoxInFrustum(tileentity1.renderBoundingBox)) continue
                    val blockpos3 = tileentity1.pos
                    var irendertypebuffer1: IRenderTypeBuffer? = `irendertypebuffer$impl`
                    matrixStackIn.push()
                    matrixStackIn.translate(blockpos3.x.toDouble() - d0, blockpos3.y.toDouble() - d1, blockpos3.z.toDouble() - d2)
                    val sortedset = damageProgress[blockpos3.toLong()]
                    if (sortedset != null && !sortedset.isEmpty()) {
                        val j3 = sortedset.last().partialBlockDamage
                        if (j3 >= 0) {
                            val `matrixstack$entry` = matrixStackIn.last
                            val ivertexbuilder: IVertexBuilder = MatrixApplyingVertexBuilder(renderTypeTextures.crumblingBufferSource.getBuffer(ModelBakery.DESTROY_RENDER_TYPES[j3]), `matrixstack$entry`.matrix, `matrixstack$entry`.normal)
                            irendertypebuffer1 = IRenderTypeBuffer { p_230014_2_: RenderType ->
                                val ivertexbuilder3 = `irendertypebuffer$impl`.getBuffer(p_230014_2_)
                                if (p_230014_2_.isUseDelegate) VertexBuilderUtils.newDelegate(ivertexbuilder, ivertexbuilder3) else ivertexbuilder3
                            }
                        }
                    }
                    TileEntityRendererDispatcher.instance.renderTileEntity(tileentity1, partialTicks, matrixStackIn, irendertypebuffer1)
                    matrixStackIn.pop()
                }
            }
        }
        synchronized(setTileEntities) {
            for (tileentity in setTileEntities) {
                if (!clippinghelper.isBoundingBoxInFrustum(tileentity.renderBoundingBox)) continue
                val blockpos2 = tileentity.pos
                matrixStackIn.push()
                matrixStackIn.translate(blockpos2.x.toDouble() - d0, blockpos2.y.toDouble() - d1, blockpos2.z.toDouble() - d2)
                TileEntityRendererDispatcher.instance.renderTileEntity(tileentity, partialTicks, matrixStackIn, `irendertypebuffer$impl`)
                matrixStackIn.pop()
            }
        }
        checkMatrixStack(matrixStackIn)
        `irendertypebuffer$impl`.finish(RenderType.getSolid())
        `irendertypebuffer$impl`.finish(Atlases.getSolidBlockType())
        `irendertypebuffer$impl`.finish(Atlases.getCutoutBlockType())
        `irendertypebuffer$impl`.finish(Atlases.getBedType())
        `irendertypebuffer$impl`.finish(Atlases.getShulkerBoxType())
        `irendertypebuffer$impl`.finish(Atlases.getSignType())
        `irendertypebuffer$impl`.finish(Atlases.getChestType())
        renderTypeTextures.outlineBufferSource.finish()
        if (flag2) {
            entityOutlineShader!!.render(partialTicks)
            // changed to our framebuffer
            mainFramebuffer.bindFramebuffer(false)
        }
        iprofiler.endStartSection("destroyProgress")
        for (entry in damageProgress.long2ObjectEntrySet()) {
            val blockpos1 = BlockPos.fromLong(entry.longKey)
            val d3 = blockpos1.x.toDouble() - d0
            val d4 = blockpos1.y.toDouble() - d1
            val d5 = blockpos1.z.toDouble() - d2
            if (d3 * d3 + d4 * d4 + d5 * d5 <= 1024.0) {
                val sortedset1 = entry.value
                if (sortedset1 != null && !sortedset1.isEmpty()) {
                    val k3 = sortedset1.last().partialBlockDamage
                    matrixStackIn.push()
                    matrixStackIn.translate(blockpos1.x.toDouble() - d0, blockpos1.y.toDouble() - d1, blockpos1.z.toDouble() - d2)
                    val `matrixstack$entry1` = matrixStackIn.last
                    val ivertexbuilder1: IVertexBuilder = MatrixApplyingVertexBuilder(renderTypeTextures.crumblingBufferSource.getBuffer(ModelBakery.DESTROY_RENDER_TYPES[k3]), `matrixstack$entry1`.matrix, `matrixstack$entry1`.normal)
                    this.mc.blockRendererDispatcher.renderBlockDamage(world.getBlockState(blockpos1), blockpos1, world, matrixStackIn, ivertexbuilder1)
                    matrixStackIn.pop()
                }
            }
        }
        checkMatrixStack(matrixStackIn)
//        val raytraceresult = this.mc.objectMouseOver
//        if (drawBlockOutline && raytraceresult != null && raytraceresult.type == RayTraceResult.Type.BLOCK) {
//            iprofiler.endStartSection("outline")
//            val blockpos = (raytraceresult as BlockRayTraceResult).pos
//            val blockstate = world.getBlockState(blockpos)
//            if (!ForgeHooksClient.onDrawBlockHighlight(this, activeRenderInfoIn, raytraceresult, partialTicks, matrixStackIn, `irendertypebuffer$impl`)) if (!blockstate.isAir(world, blockpos) && world.worldBorder.contains(blockpos)) {
//                val ivertexbuilder2 = `irendertypebuffer$impl`.getBuffer(RenderType.getLines())
//                drawSelectionBox(matrixStackIn, ivertexbuilder2, activeRenderInfoIn.renderViewEntity, d0, d1, d2, blockpos, blockstate)
//            }
//        } else if (raytraceresult != null && raytraceresult.type == RayTraceResult.Type.ENTITY) {
//            ForgeHooksClient.onDrawBlockHighlight(this, activeRenderInfoIn, raytraceresult, partialTicks, matrixStackIn, `irendertypebuffer$impl`)
//        }
//        RenderSystem.pushMatrix()
//        RenderSystem.multMatrix(matrixStackIn.last.matrix)
//        this.mc.debugRenderer.render(matrixStackIn, `irendertypebuffer$impl`, d0, d1, d2)
//        RenderSystem.popMatrix()
        `irendertypebuffer$impl`.finish(Atlases.getTranslucentCullBlockType())
        `irendertypebuffer$impl`.finish(Atlases.getBannerType())
        `irendertypebuffer$impl`.finish(Atlases.getShieldType())
        `irendertypebuffer$impl`.finish(RenderType.getArmorGlint())
        `irendertypebuffer$impl`.finish(RenderType.getArmorEntityGlint())
        `irendertypebuffer$impl`.finish(RenderType.getGlint())
        `irendertypebuffer$impl`.finish(RenderType.getGlintDirect())
        `irendertypebuffer$impl`.finish(RenderType.getGlintTranslucent())
        `irendertypebuffer$impl`.finish(RenderType.getEntityGlint())
        `irendertypebuffer$impl`.finish(RenderType.getEntityGlintDirect())
        `irendertypebuffer$impl`.finish(RenderType.getWaterMask())
        renderTypeTextures.crumblingBufferSource.finish()
        if (field_239227_K_ != null) {
            `irendertypebuffer$impl`.finish(RenderType.getLines())
            `irendertypebuffer$impl`.finish()
            field_239222_F_!!.framebufferClear(Minecraft.IS_RUNNING_ON_MAC)
            field_239222_F_!!.func_237506_a_(this.mc.framebuffer)
            iprofiler.endStartSection("translucent")
            renderBlockLayer(RenderType.getTranslucent(), matrixStackIn, d0, d1, d2)
            iprofiler.endStartSection("string")
            renderBlockLayer(RenderType.getTripwire(), matrixStackIn, d0, d1, d2)
            field_239224_H_!!.framebufferClear(Minecraft.IS_RUNNING_ON_MAC)
            field_239224_H_!!.func_237506_a_(this.mc.framebuffer)

            // TODO: Particles
//            RenderState.field_239237_T_.setupRenderState()
//            iprofiler.endStartSection("particles")
//            this.mc.particles.renderParticles(matrixStackIn, `irendertypebuffer$impl`, lightmapIn, activeRenderInfoIn, partialTicks, clippinghelper)
//            RenderState.field_239237_T_.clearRenderState()
        } else {
            iprofiler.endStartSection("translucent")
            renderBlockLayer(RenderType.getTranslucent(), matrixStackIn, d0, d1, d2)
            `irendertypebuffer$impl`.finish(RenderType.getLines())
            `irendertypebuffer$impl`.finish()
            iprofiler.endStartSection("string")
            renderBlockLayer(RenderType.getTripwire(), matrixStackIn, d0, d1, d2)
            // TODO: Particles
//            iprofiler.endStartSection("particles")
//            this.mc.particles.renderParticles(matrixStackIn, `irendertypebuffer$impl`, lightmapIn, activeRenderInfoIn, partialTicks, clippinghelper)
        }
        RenderSystem.pushMatrix()
        RenderSystem.multMatrix(matrixStackIn.last.matrix)
        if (this.mc.gameSettings.getCloudOption() != CloudOption.OFF) {
            if (field_239227_K_ != null) {
                field_239226_J_!!.framebufferClear(Minecraft.IS_RUNNING_ON_MAC)
                RenderState.field_239239_V_.setupRenderState()
                iprofiler.endStartSection("clouds")
                renderClouds(matrixStackIn, partialTicks, d0, d1, d2)
                RenderState.field_239239_V_.clearRenderState()
            } else {
                iprofiler.endStartSection("clouds")
                renderClouds(matrixStackIn, partialTicks, d0, d1, d2)
            }
        }
        if (field_239227_K_ != null) {
            RenderState.field_239238_U_.setupRenderState()
            iprofiler.endStartSection("weather")
            renderRainSnow(lightmapIn, partialTicks, d0, d1, d2)
//            renderWorldBorder(activeRenderInfoIn)
            RenderState.field_239238_U_.clearRenderState()
            field_239227_K_!!.render(partialTicks)
            // changed to our framebuffer
            mainFramebuffer.bindFramebuffer(false)
        } else {
            RenderSystem.depthMask(false)
            iprofiler.endStartSection("weather")
            renderRainSnow(lightmapIn, partialTicks, d0, d1, d2)
//            renderWorldBorder(activeRenderInfoIn)
            RenderSystem.depthMask(true)
        }
//        renderDebug(activeRenderInfoIn)
        RenderSystem.shadeModel(7424)
        RenderSystem.depthMask(true)
        RenderSystem.disableBlend()
        RenderSystem.popMatrix()
        FogRenderer.resetFog()
    }

//    override fun makeEntityOutlineShader() {
//        if (entityOutlineShader != null) {
//            entityOutlineShader!!.close()
//        }
//        val resourcelocation = ResourceLocation("shaders/post/entity_outline.json")
//        try {
//            // changed to our framebuffer
//            entityOutlineShader = ShaderGroup(mc.getTextureManager(), mc.resourceManager, mainFramebuffer, resourcelocation)
//            entityOutlineShader!!.createBindFramebuffers(mainFramebuffer.framebufferWidth, mainFramebuffer.framebufferHeight)
//            entityOutlineFramebuffer = entityOutlineShader!!.getFramebufferRaw("final")
//        } catch (ioexception: IOException) {
//            LOGGER.warn("Failed to load shader: {}", resourcelocation, ioexception)
//            entityOutlineShader = null
//            entityOutlineFramebuffer = null
//        } catch (jsonsyntaxexception: JsonSyntaxException) {
//            LOGGER.warn("Failed to parse shader: {}", resourcelocation, jsonsyntaxexception)
//            entityOutlineShader = null
//            entityOutlineFramebuffer = null
//        }
//    }

//    override fun func_239233_v_() {
//        func_239234_w_()
//        val resourcelocation = ResourceLocation("shaders/post/transparency.json")
//        try {
//            // changed to our framebuffer
//            val shadergroup = ShaderGroup(mc.getTextureManager(), mc.resourceManager, mainFramebuffer, resourcelocation)
//            shadergroup.createBindFramebuffers(mainFramebuffer.framebufferWidth, mainFramebuffer.framebufferHeight)
//            val framebuffer1 = shadergroup.getFramebufferRaw("translucent")
//            val framebuffer2 = shadergroup.getFramebufferRaw("itemEntity")
//            val framebuffer3 = shadergroup.getFramebufferRaw("particles")
//            val framebuffer4 = shadergroup.getFramebufferRaw("weather")
//            val framebuffer = shadergroup.getFramebufferRaw("clouds")
//            field_239227_K_ = shadergroup
//            field_239222_F_ = framebuffer1
//            field_239223_G_ = framebuffer2
//            field_239224_H_ = framebuffer3
//            field_239225_I_ = framebuffer4
//            field_239226_J_ = framebuffer
//        } catch (exception: Exception) {
//            val s = if (exception is JsonSyntaxException) "parse" else "load"
//            val s1 = "Failed to $s shader: $resourcelocation"
//            val `worldrenderer$shaderexception` = ShaderException(s1, exception)
//            if (mc.resourcePackList.func_232621_d_().size > 1) {
//                val itextcomponent: ITextComponent?
//                itextcomponent = try {
//                    StringTextComponent(mc.resourceManager.getResource(resourcelocation).packName)
//                } catch (ioexception: IOException) {
//                    null
//                }
//                mc.gameSettings.graphicFanciness = GraphicsFanciness.FANCY
//                mc.throwResourcePackLoadError(`worldrenderer$shaderexception`, itextcomponent)
//            } else {
//                val crashreport = mc.addGraphicsAndWorldToCrashReport(CrashReport(s1, `worldrenderer$shaderexception`))
//                mc.gameSettings.graphicFanciness = GraphicsFanciness.FANCY
//                mc.gameSettings.saveOptions()
//                LOGGER.fatal(s1, `worldrenderer$shaderexception` as Throwable)
//                mc.freeMemory()
//                Minecraft.displayCrashReport(crashreport)
//            }
//        }
//    }

}