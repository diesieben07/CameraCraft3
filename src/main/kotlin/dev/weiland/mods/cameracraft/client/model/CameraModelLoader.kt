package dev.weiland.mods.cameracraft.client.model

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonObject
import com.mojang.datafixers.util.Pair
import dev.weiland.mods.cameracraft.CameraCraft
import dev.weiland.mods.cameracraft.blocks.CameraTile
import net.minecraft.block.BlockState
import net.minecraft.client.renderer.model.*
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.resources.IResourceManager
import net.minecraft.util.Direction
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.vector.Quaternion
import net.minecraft.util.math.vector.TransformationMatrix
import net.minecraftforge.client.model.IModelConfiguration
import net.minecraftforge.client.model.IModelLoader
import net.minecraftforge.client.model.SimpleModelTransform
import net.minecraftforge.client.model.data.EmptyModelData
import net.minecraftforge.client.model.data.IModelData
import net.minecraftforge.client.model.geometry.IModelGeometry
import java.util.*
import java.util.function.Function

internal object CameraModelLoader : IModelLoader<CameraModelLoader.Geometry> {

    val ID = ResourceLocation(CameraCraft.MOD_ID, "camera_model")

    override fun onResourceManagerReload(resourceManager: IResourceManager) {
    }

    override fun read(deserializationContext: JsonDeserializationContext, modelContents: JsonObject): Geometry {
        val model = modelContents.getAsJsonObject("model")
        val delegate = deserializationContext.deserialize<BlockModel>(model, BlockModel::class.java)
        return Geometry(delegate)
    }

    internal class Geometry(private val delegate: BlockModel) : IModelGeometry<Geometry> {

        override fun bake(owner: IModelConfiguration?, bakery: ModelBakery, spriteGetter: Function<RenderMaterial, TextureAtlasSprite>, modelTransform: IModelTransform, overrides: ItemOverrideList, modelLocation: ResourceLocation): IBakedModel {
            return Baked(
                    delegate, bakery, spriteGetter, modelTransform, modelLocation, overrides
            )
        }

        override fun getTextures(owner: IModelConfiguration, modelGetter: Function<ResourceLocation, IUnbakedModel>, missingTextureErrors: MutableSet<Pair<String, String>>): MutableCollection<RenderMaterial> {
            return delegate.getMaterials(modelGetter, missingTextureErrors)
        }
    }

    private class Baked(
            private val base: BlockModel,
            private val bakery: ModelBakery,
            private val spriteGetter: Function<RenderMaterial, TextureAtlasSprite>,
            private val baseTransform: IModelTransform,
            private val modelLocation: ResourceLocation,
            private val overrides: ItemOverrideList
    ) : IBakedModel {

        private val cache: Array<IBakedModel?> = arrayOfNulls(CameraTile.ROTATION_STEPS)

        private fun getModel(data: IModelData): IBakedModel {
            val rotation = if (data is CameraTile.ModelData) data.rotation else 0
            return getModel(rotation)
        }

        private fun getModel(rotation: Int): IBakedModel {
            val cached = cache[rotation]
            if (cached != null) {
                return cached
            }
            val fullTransform = baseTransform.rotation.compose(TransformationMatrix(null, Quaternion(0f, rotation.toFloat() * 10f, 0f, true), null, null))
            val baked = base.bake(bakery, base, spriteGetter, SimpleModelTransform(fullTransform), modelLocation, true)
            cache[rotation] = baked
            return baked
        }

        override fun getQuads(state: BlockState?, side: Direction?, rand: Random): MutableList<BakedQuad> {
            return getQuads(state, side, rand, EmptyModelData.INSTANCE)
        }

        override fun getQuads(state: BlockState?, side: Direction?, rand: Random, extraData: IModelData): MutableList<BakedQuad> {
            return getModel(extraData).getQuads(state, side, rand, extraData)
        }

        override fun useAmbientOcclusion(): Boolean {
            return getModel(0).useAmbientOcclusion()
        }

        override fun isAmbientOcclusion(state: BlockState?): Boolean {
            return getModel(0).isAmbientOcclusion(state)
        }

        override fun getParticleIcon(): TextureAtlasSprite {
            return getModel(0).getParticleTexture(EmptyModelData.INSTANCE)
        }

        override fun getParticleTexture(data: IModelData): TextureAtlasSprite {
            return getModel(data).getParticleTexture(data)
        }

        override fun isGui3d(): Boolean {
            return getModel(0).isGui3d
        }

        override fun usesBlockLight(): Boolean {
            return getModel(0).usesBlockLight()
        }

        override fun isCustomRenderer(): Boolean {
            return false
        }

        override fun getOverrides(): ItemOverrideList {
            return overrides
        }
    }

}