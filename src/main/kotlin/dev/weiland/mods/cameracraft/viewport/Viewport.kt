package dev.weiland.mods.cameracraft.viewport

import net.minecraft.entity.Entity
import net.minecraft.network.IPacket
import net.minecraft.util.RegistryKey
import net.minecraft.world.World

internal class Viewport(
    val dimension: RegistryKey<World>,
    val entity: Entity
) {

    fun sendPacket(packet: IPacket<*>) {
        // TODO
    }


    private fun cleanup() {

    }

    companion object {

//        private val ownerQueue = ReferenceQueue<Any>()
//
//        init {
//            thread(isDaemon = true) {
//                while (true) {
//                    val ref = ownerQueue.remove()
//                    (ref as Viewport).cleanup()
//                }
//            }
//        }
//
//        fun createForEntity(entity: Entity): Viewport {
//            TODO()
//        }
    }

}