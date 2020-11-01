package dev.weiland.mods.cameracraft.network

import dev.weiland.mods.cameracraft.client.fakeworld.ClientViewportManager
import net.minecraft.client.Minecraft
import net.minecraft.client.network.play.IClientPlayNetHandler
import net.minecraft.network.IPacket
import net.minecraft.network.PacketBuffer
import net.minecraft.network.PacketDirection
import net.minecraft.network.ProtocolType
import net.minecraft.util.MinecraftVersion
import net.minecraft.util.RegistryKey
import net.minecraft.util.registry.Registry
import net.minecraft.world.World
import org.apache.logging.log4j.LogManager

internal class RedirectedPacket private constructor(
        private val packet: IPacket<IClientPlayNetHandler>?,
        private val dimensionKey: RegistryKey<World>
) {

    constructor(dimensionKey: RegistryKey<World>, packet: IPacket<IClientPlayNetHandler>) : this(packet, dimensionKey)

    companion object {

        private val LOGGER = LogManager.getLogger()

        fun read(buf: PacketBuffer): RedirectedPacket {
            val dimensionKey: RegistryKey<World> = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, buf.readResourceLocation())
            val vanillaPacketId = buf.readVarInt()
            val vanillaPacket = ProtocolType.PLAY.getPacket(PacketDirection.CLIENTBOUND, vanillaPacketId)
            vanillaPacket?.readPacketData(buf)
            @Suppress("UNCHECKED_CAST")
            return RedirectedPacket(vanillaPacket as IPacket<IClientPlayNetHandler>?, dimensionKey)
        }
    }

    fun write(buf: PacketBuffer) {
        buf.writeResourceLocation(dimensionKey.location)
        checkNotNull(packet).writePacketData(buf)
    }

    fun handle() {
        val vanillaPacket = packet ?: run {
            LOGGER.warn("Received invalid RedirectedPacket")
            return
        }
        val connection = checkNotNull(Minecraft.getInstance().connection) {
            "Received a packet when Minecraft.connection was null. ???!!!"
        }
        ClientViewportManager.runWithContext(dimensionKey) {
            vanillaPacket.processPacket(connection)
        }
    }

}