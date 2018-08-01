package de.takeweiland.mods.cameracraft.io

import de.takeweiland.mods.cameracraft.api.io.ImageIO
import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufInputStream
import io.netty.buffer.ByteBufOutputStream
import java.awt.image.BufferedImage
import java.io.InputStream
import java.io.OutputStream
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import javax.imageio.ImageIO as NativeImageIO

/**
 * @author Take Weiland
 */
object DefaultImageIO : ImageIO {

    override fun read(file: Path): BufferedImage {
        return if (file.fileSystem === FileSystems.getDefault()) {
            NativeImageIO.read(file.toFile())
        } else {
            Files.newInputStream(file).use { input ->
                NativeImageIO.read(input)
            }
        }
    }

    override fun read(input: InputStream): BufferedImage {
        return NativeImageIO.read(input)
    }

    override fun read(buf: ByteBuf): BufferedImage {
        return ByteBufInputStream(buf).use { input ->
            NativeImageIO.read(input)
        }
    }

    override fun write(file: Path, image: BufferedImage) {
        if (file.fileSystem === FileSystems.getDefault()) {
            NativeImageIO.write(image, "PNG", file.toFile())
        } else {
            Files.newOutputStream(file).use { output ->
                NativeImageIO.write(image, "PNG", output)
            }
        }
    }

    override fun write(output: OutputStream, image: BufferedImage) {
        NativeImageIO.write(image, "PNG", output)
    }

    override fun write(buf: ByteBuf, image: BufferedImage) {
        ByteBufOutputStream(buf).use { output ->
            NativeImageIO.write(image, "PNG", output)
        }
    }
}