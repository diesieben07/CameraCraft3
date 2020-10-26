package dev.weiland.mods.cameracraft.client.model

import com.mojang.datafixers.util.Pair
import net.minecraft.client.renderer.model.*
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.vector.Matrix4f
import net.minecraft.util.math.vector.Quaternion
import net.minecraft.util.math.vector.TransformationMatrix
import net.minecraftforge.client.model.IModelConfiguration
import net.minecraftforge.client.model.ModelTransformComposition
import net.minecraftforge.client.model.geometry.IModelGeometry
import net.minecraftforge.client.model.geometry.IModelGeometryPart
import java.util.*
import java.util.function.Function

internal class RotatedModelGeometry(
    private val delegate: BlockModel,
    private val yRotation: Float
) : IModelGeometry<RotatedModelGeometry> {

    class SimpleTransform(private val matrix: TransformationMatrix) : IModelTransform {

        override fun getRotation(): TransformationMatrix {
            return matrix
        }
    }

    override fun bake(
        owner: IModelConfiguration?, bakery: ModelBakery, spriteGetter: Function<RenderMaterial, TextureAtlasSprite>, modelTransform: IModelTransform,
        overrides: ItemOverrideList?, modelLocation: ResourceLocation
    ): IBakedModel {
        val transform = ModelTransformComposition(
            modelTransform,
            SimpleTransform(
                TransformationMatrix(
                    null,
                    Quaternion(0f, yRotation, 0f, true),
                    null,
                    null
                )
            )
        )

        return delegate.bakeModel(
            bakery, delegate, spriteGetter, transform, modelLocation, true
        )

//        return delegate.bake(
//            owner, bakery, spriteGetter, modelTransform, overrides, modelLocation
//        )
    }

    override fun getTextures(
        owner: IModelConfiguration?, modelGetter: Function<ResourceLocation, IUnbakedModel>, missingTextureErrors: MutableSet<Pair<String, String>>
    ): MutableCollection<RenderMaterial> {
        return delegate.getTextures(modelGetter, missingTextureErrors)
    }
}