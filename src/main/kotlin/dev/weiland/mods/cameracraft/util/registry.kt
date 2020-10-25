package dev.weiland.mods.cameracraft.util

import net.minecraftforge.fml.RegistryObject
import net.minecraftforge.registries.IForgeRegistryEntry
import kotlin.reflect.KProperty

internal operator fun <T : IForgeRegistryEntry<in T>> RegistryObject<T>.getValue(thisRef: Any?, prop: KProperty<*>): T {
    return get()
}