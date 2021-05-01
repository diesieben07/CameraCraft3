package dev.weiland.mods.cameracraft.viewport

import dev.weiland.mods.cameracraft.CameraCraft
import dev.weiland.mods.cameracraft.network.RedirectedPacket
import net.minecraft.client.network.play.IClientPlayNetHandler
import net.minecraft.entity.Entity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.network.IPacket
import net.minecraft.network.play.ServerPlayNetHandler
import net.minecraft.util.RegistryKey
import net.minecraft.world.World
import net.minecraftforge.fml.network.NetworkDirection
import net.minecraftforge.fml.network.PacketDistributor

internal class Viewport(
    val dimension: RegistryKey<World>,
    val entity: Entity,
    val managingPlayer: ServerPlayerEntity
) {

    fun sendPacket(packet: IPacket<IClientPlayNetHandler>) {
        managingPlayer.connection.send(
                CameraCraft.NETWORK.toVanillaPacket(RedirectedPacket(dimension, packet), NetworkDirection.PLAY_TO_CLIENT)
        )
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