package de.takeweiland.mods.cameracraft.api.store

import kotlinx.coroutines.experimental.Deferred
import java.awt.image.BufferedImage
import java.util.*

/**
 * @author Take Weiland
 */
interface PhotoStore {

    fun getImage(id: UUID): Deferred<BufferedImage>

    fun getMetadata(id: UUID): PhotoMetadata

    fun exists(id: UUID): Deferred<Boolean>

    fun delete(id: UUID): Deferred<Boolean
            >

}