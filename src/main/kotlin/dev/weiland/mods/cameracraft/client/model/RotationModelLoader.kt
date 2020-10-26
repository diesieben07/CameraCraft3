package dev.weiland.mods.cameracraft.client.model

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonObject
import dev.weiland.mods.cameracraft.CameraCraft
import net.minecraft.client.renderer.model.BlockModel
import net.minecraft.resources.IResourceManager
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.IModelLoader
import net.minecraftforge.client.model.ModelLoaderRegistry

internal object RotationModelLoader : IModelLoader<RotatedModelGeometry> {

    val ID = ResourceLocation(CameraCraft.MOD_ID, "rotate_y")

    override fun onResourceManagerReload(resourceManager: IResourceManager) {
    }

    override fun read(deserializationContext: JsonDeserializationContext, modelContents: JsonObject): RotatedModelGeometry {
        val rotation = modelContents.getAsJsonPrimitive("rotation_y").asFloat
        val model = modelContents.getAsJsonObject("model")
        val delegate = deserializationContext.deserialize<BlockModel>(model, BlockModel::class.java)

        return RotatedModelGeometry(
            delegate,
            rotation
        )
    }
}