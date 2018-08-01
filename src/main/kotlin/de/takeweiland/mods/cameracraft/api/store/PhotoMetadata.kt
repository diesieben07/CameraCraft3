package de.takeweiland.mods.cameracraft.api.store

import java.util.*
import net.minecraft.entity.Entity as MCEntity

/**
 * @author Take Weiland
 */
data class PhotoMetadata(val id: UUID, val owningPlayer: UUID?, val position: WorldPosition, val source: PhotoSource)

data class WorldPosition(val dimension: Int, val x: Double, val y: Double, val z: Double)
data class BlockPosition(val x: Int, val y: Int, val z: Int)

sealed class PhotoSource {

    data class Player(val id: UUID) : PhotoSource()
    data class Block(val state: String, val position: BlockPosition) : PhotoSource()
    data class Entity(val id: UUID, val type: String)

}