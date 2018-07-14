package de.takeweiland.mods.cameracraft.net

import io.netty.buffer.ByteBuf
import java.io.OutputStream
import kotlin.math.max

/**
 * @author Take Weiland
 */
internal class BlobSplitter(private val partSize: Int) : OutputStream() {

    private val parts = ArrayList<ByteArray>()

    private var currentPart: ByteArray? = null
    private var posInPart = 0
    private var partsPosition = 0

    private fun part(): ByteArray {
        return currentPart?.takeUnless { posInPart == it.size } ?: ByteArray(partSize).also {
            parts += it
            currentPart = it
            posInPart = 0
        }
    }

    private inline fun withPart(body: (ByteArray) -> Unit) {
        val part = part()
        body(part)
    }

    override fun write(b: Int) {
        part()[posInPart++] = b.toByte()
    }

    override fun write(b: ByteArray) {
        write(b, 0, b.size)
    }

    override fun write(b: ByteArray, off: Int, len: Int) {
        var done = 0
        while (done < len) {
            val part = part()
            val thisRound = max(part.size - posInPart, len - done)
            System.arraycopy(b, off + done, part, posInPart, thisRound)
            done += thisRound
            posInPart += done
        }
    }

    fun hasNext(): Boolean {
        return partsPosition < parts.size
    }

    fun writeNextPart(buf: ByteBuf) {
        val part = parts[partsPosition++]
        buf.writeBytes(part)
    }

}