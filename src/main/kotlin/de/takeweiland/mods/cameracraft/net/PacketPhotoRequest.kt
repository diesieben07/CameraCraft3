package de.takeweiland.mods.cameracraft.net

import com.sun.scenario.effect.impl.prism.ps.PPSRenderer
import de.takeweiland.mods.commons.net.PacketWithResponse
import de.takeweiland.mods.commons.net.ResponsePacket
import de.takeweiland.mods.commons.net.codec.readString
import de.takeweiland.mods.commons.net.codec.readUUID
import de.takeweiland.mods.commons.net.codec.writeString
import de.takeweiland.mods.commons.net.codec.writeUUID
import io.netty.buffer.ByteBuf
import kotlinx.coroutines.experimental.Deferred
import net.minecraft.entity.player.EntityPlayer
import java.util.*

/**
 * @author Take Weiland
 */
internal class PacketPhotoRequest : PacketWithResponse.Async<PacketPhotoRequest.Response> {

    private val requestId: Int

    constructor(requestId: Int) {
        this.requestId = requestId
    }

    constructor(buf: ByteBuf) {
        this.requestId = buf.readInt()
    }

    override fun write(buf: ByteBuf) {
        buf.writeInt(requestId)
    }

    override fun process(player: EntityPlayer): Deferred<Response> {

    }

    internal class Response : ResponsePacket {

        private val data: ResponseData

        constructor(data: ResponseData) {
            this.data = data
        }

        constructor(buf: ByteBuf) {
            this.data = when (buf.readByte().toInt()) {
                0 -> ResponseData.Success
                1 -> ResponseData.Failure(buf.readString())
                else -> throw IllegalStateException("Invalid response data type received")
            }
        }

        override fun write(buf: ByteBuf) {
            when (data) {
                is ResponseData.Success -> {
                    buf.writeByte(0)
                }
                is ResponseData.Failure -> {
                    buf.writeByte(1)
                    buf.writeString(data.reason)
                }
            }
        }

    }

    internal sealed class ResponseData {

        object Success : ResponseData()

        data class Failure(val reason: String) : ResponseData()

    }

}