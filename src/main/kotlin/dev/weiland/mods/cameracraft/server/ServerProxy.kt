package dev.weiland.mods.cameracraft.server

import dev.weiland.mods.cameracraft.CCProxy

@Suppress("unused")
class ServerProxy : CCProxy {

    override fun createOverlayForEntity(entityId: Int) {
        throw IllegalStateException()
    }
}