package de.takeweiland.mods.cameracraft.api.io

import io.netty.buffer.ByteBuf
import java.awt.image.BufferedImage
import java.io.InputStream
import java.io.OutputStream
import java.nio.file.Path

/**
 * @author Take Weiland
 */
interface ImageIO {

    fun read(file: Path): BufferedImage

    fun read(input: InputStream): BufferedImage

    fun read(buf: ByteBuf): BufferedImage

    fun write(file: Path, image: BufferedImage)
    fun write(output: OutputStream, image: BufferedImage)
    fun write(buf: ByteBuf, image: BufferedImage)

}