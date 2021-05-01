package dev.weiland.mods.cameracraft.viewport

import net.minecraft.entity.Entity
import net.minecraft.network.IPacket
import net.minecraft.util.RegistryKey
import net.minecraft.util.math.ChunkPos
import net.minecraft.world.World
import net.minecraft.world.server.ServerWorld

internal class ServerViewportManager(private val world: ServerWorld) {

    private val instances = HashMap<ChunkPos, Instance>()

    class Instance {
        
        val viewports = ArrayList<Viewport>()
        
    }
    
    fun sendToTracking(chunk: ChunkPos, packet: IPacket<*>, boundaryOnly: Boolean) {
    }

    fun sendToTracking(entity: Entity, packet: IPacket<*>) {

    }

    companion object {

        private val map = HashMap<RegistryKey<World>, ServerViewportManager>()

        @JvmStatic
        fun get(world: ServerWorld): ServerViewportManager {
            return map.getOrPut(world.dimension()) { ServerViewportManager(world) }
        }
    }

}