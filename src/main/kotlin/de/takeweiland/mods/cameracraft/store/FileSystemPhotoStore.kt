package de.takeweiland.mods.cameracraft.store

import de.takeweiland.mods.cameracraft.CC_IO_THREAD
import de.takeweiland.mods.cameracraft.CameraCraft
import de.takeweiland.mods.cameracraft.api.photography.Photo
import de.takeweiland.mods.cameracraft.api.store.*
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import java.awt.image.BufferedImage
import java.io.DataInput
import java.io.DataInputStream
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

/**
 * @author Take Weiland
 */
internal class FileSystemPhotoStore(private val path: Path) : PhotoStore {

    private companion object {
        const val FLAG_HAS_OWNER = 0b1000

        const val SOURCE_MASK = 0b0111
        const val SOURCE_PLAYER = 0b0000
        const val SOURCE_BLOCK = 0b0001
        const val SOURCE_ENTITY = 0b0010

    }

    override fun getPhoto(id: UUID): Deferred<Photo> {
        return async(CC_IO_THREAD) {
            val file =
        }
    }

    override fun getImage(id: UUID): Deferred<BufferedImage> {
        return async(CC_IO_THREAD) {
            CameraCraft.imageIO.read(path(id, "png"))
        }
    }

    override fun delete(id: UUID): Deferred<Boolean> {
        return async(CC_IO_THREAD) {
            Files.deleteIfExists(path(id, "png")) || Files.deleteIfExists(path(id, "dat"))
        }
    }

    private fun path(id: UUID, suffix: String): Path {
        return path.resolve("$id.$suffix")
    }

    private fun readMetadata(id: UUID): PhotoMetadata {
        val file = path(id, "dat")
        DataInputStream(Files.newInputStream(file)).use { input ->
            val flags = input.readUnsignedByte()
            val ownerId = if ((flags and FLAG_HAS_OWNER) != 0) input.readUUID() else null

            val dim = input.readInt()
            val x = input.readDouble()
            val y = input.readDouble()
            val z = input.readDouble()

            val source = when (flags and SOURCE_MASK) {
                SOURCE_PLAYER -> PhotoSource.Player(ownerId ?: throw IOException("PhotoSource.Player requires an owning player"))
                SOURCE_BLOCK -> {
                    val state = input.readUTF()
                    val bx = input.readInt()
                    val by = input.readUnsignedByte()
                    val bz = input.readInt()
                    PhotoSource.Block(state, BlockPosition(bx, by, bz))
                }
                SOURCE_ENTITY -> {
                    val entityId = input.readUUID()
                    val
                    PhotoSource.Entity()
                }
            }
        }
    }

    private fun DataInput.readUUID(): UUID {
        val lsb = readLong()
        val msb = readLong()
        return UUID(msb, lsb)
    }

}