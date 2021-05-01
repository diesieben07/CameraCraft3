package dev.weiland.mods.cameracraft.util

import net.minecraft.nbt.INBT
import net.minecraft.util.Direction
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.CapabilityManager
import java.util.concurrent.Callable

private fun throwException(): Nothing = throw UnsupportedOperationException("This is an internal capability")

private fun <R> getStorageImpl(): Capability.IStorage<R> {
    return object : Capability.IStorage<R> {
        override fun writeNBT(capability: Capability<R>?, instance: R, side: Direction?): INBT? {
            throwException()
        }

        override fun readNBT(capability: Capability<R>?, instance: R, side: Direction?, nbt: INBT?) {
            throwException()
        }
    }
}

private fun <R> getCallableImpl(): Callable<R> {
    return Callable(::throwException)
}

internal inline fun <reified R : Any> registerInternalCapability() {
    CapabilityManager.INSTANCE.register(R::class.java, getStorageImpl(), getCallableImpl())
}