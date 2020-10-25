package dev.weiland.mods.cameracraft.client.render

import net.minecraft.client.Minecraft

internal interface GlobalState {

    fun restore(mc: Minecraft)

}