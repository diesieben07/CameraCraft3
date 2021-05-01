package dev.weiland.mods.cameracraft.util

import net.minecraft.entity.Entity
import net.minecraft.network.datasync.DataParameter
import kotlin.reflect.KProperty

internal operator fun <T> DataParameter<T>.getValue(entity: Entity, prop: KProperty<*>): T {
    return entity.entityData[this]
}

internal operator fun <T> DataParameter<T>.setValue(entity: Entity, prop: KProperty<*>, newValue: T) {
    entity.entityData.set(this, newValue)
}