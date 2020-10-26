package dev.weiland.mods.cameracraft.storage

import dev.weiland.mods.cameracraft.CameraCraft
import dev.weiland.mods.cameracraft.api.PhotoImageData
import dev.weiland.mods.cameracraft.api.PhotoMetadata
import dev.weiland.mods.cameracraft.api.PhotoStorage
import net.minecraft.nbt.CompoundNBT
import net.minecraft.world.storage.WorldSavedData

internal class DefaultWorldPhotoStorage : WorldSavedData(name), PhotoStorage {

    private companion object {

        const val name = "${CameraCraft.MOD_ID}_photos"

    }

    override fun read(nbt: CompoundNBT) {

    }

    override fun write(compound: CompoundNBT): CompoundNBT {
        TODO("Not yet implemented")
    }

    override fun store(id: String, image: PhotoImageData, metadata: PhotoMetadata) {
        TODO("Not yet implemented")
    }

}