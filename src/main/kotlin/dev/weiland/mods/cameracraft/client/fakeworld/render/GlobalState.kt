package dev.weiland.mods.cameracraft.client.fakeworld.render

import net.minecraft.client.Minecraft

internal interface GlobalState {

    fun restore(mc: Minecraft)

}