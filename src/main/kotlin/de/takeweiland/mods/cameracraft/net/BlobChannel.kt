package de.takeweiland.mods.cameracraft.net

import de.takeweiland.mods.commons.netbase.CustomPayloadHandler
import io.netty.buffer.ByteBuf
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.fml.relauncher.Side

/**
 * @author Take Weiland
 */
object BlobChannel : CustomPayloadHandler {

    override fun handle(channel: String, buf: ByteBuf, side: Side, player: EntityPlayer?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}