package dev.weiland.mods.cameracraft.network

import dev.weiland.mods.cameracraft.CCProxy
import net.minecraft.network.PacketBuffer

internal class CreateViewportPacket(
    private val entityId: Int
) {

    fun write(buf: PacketBuffer) {
        buf.writeInt(entityId)
    }

    fun handle() {
        CCProxy.createOverlayForEntity(entityId)
    }

    companion object {
        fun read(buf: PacketBuffer): CreateViewportPacket {
            val entityId = buf.readInt()
            return CreateViewportPacket(entityId)
        }
    }

}