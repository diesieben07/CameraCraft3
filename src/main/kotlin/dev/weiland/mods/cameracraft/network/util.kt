package dev.weiland.mods.cameracraft.network

import net.minecraft.network.IPacket
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.ChunkPos
import net.minecraft.world.server.ServerWorld

internal fun IPacket<*>.sendToAllTracking(te: TileEntity) {
    val pos = te.blockPos ?: return
    val world = te.level as? ServerWorld ?: return
    val players = world.chunkSource.chunkMap.getPlayers(ChunkPos(pos), false)
    for (player in players) {
        player.connection.send(this)
    }
}