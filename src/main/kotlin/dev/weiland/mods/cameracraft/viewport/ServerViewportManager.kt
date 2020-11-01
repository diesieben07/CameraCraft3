package dev.weiland.mods.cameracraft.viewport

import net.minecraft.entity.Entity
import net.minecraft.network.IPacket
import net.minecraft.util.math.ChunkPos
import net.minecraft.world.server.ServerWorld

internal class ServerViewportManager {

    fun getViewportsTracking(chunk: ChunkPos): Iterable<Viewport> {
        // TODO
        return emptyList()
    }

    fun sendToTracking(chunk: ChunkPos, packet: IPacket<*>, boundaryOnly: Boolean) {
        println("send $packet to tracking $chunk")
    }

    fun sendToTracking(entity: Entity, packet: IPacket<*>) {
        println("send $packet to tracking $entity")
    }

    companion object {
        private val instance = ServerViewportManager()

        @JvmStatic
        fun get(/*overworld: ServerWorld*/): ServerViewportManager {
            return instance
        }
    }

}