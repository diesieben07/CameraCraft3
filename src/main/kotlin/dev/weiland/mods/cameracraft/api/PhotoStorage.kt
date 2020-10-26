package dev.weiland.mods.cameracraft.api

interface PhotoStorage {

    fun store(id: Long, image: PhotoImageData, metadata: PhotoMetadata)

}