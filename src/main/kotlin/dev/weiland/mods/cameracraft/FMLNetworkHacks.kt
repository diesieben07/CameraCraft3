package dev.weiland.mods.cameracraft

import dev.weiland.mods.cameracraft.viewport.ServerViewportManager
import net.minecraft.network.IPacket
import net.minecraft.world.chunk.Chunk
import net.minecraftforge.fml.common.ObfuscationReflectionHelper
import net.minecraftforge.fml.network.PacketDistributor
import org.apache.logging.log4j.LogManager
import java.util.function.BiFunction
import java.util.function.Consumer
import java.util.function.Supplier

private typealias PDFunctor<T> = BiFunction<PacketDistributor<T>, Supplier<T>, Consumer<IPacket<*>>>

internal object FMLNetworkHacks {

    private val LOGGER = LogManager.getLogger()

    fun init() {
        try {
            overrideTrackingChunk()
        } catch (ex: Exception) {
            LOGGER.warn("Failed to hack PacketDistributor - some modded features might not work in CameraCraft viewports.")
        }
    }

    private fun overrideTrackingChunk() {
        val existingFunctor = PacketDistributor.TRACKING_CHUNK.functor()
        val newFunctor: PDFunctor<Chunk> = PDFunctor { self, chunkSupplier ->
            val existingConsumer = existingFunctor.apply(self, chunkSupplier)
            val chunk = chunkSupplier.get()
            existingConsumer.andThen { packet ->
                ServerViewportManager.get().sendToTracking(chunk.pos, packet, false)
            }
        }
        PacketDistributor.TRACKING_CHUNK.setFunctor(newFunctor)
    }

    private fun <T> PacketDistributor<T>.functor(): PDFunctor<T> {
        return checkNotNull(
                ObfuscationReflectionHelper.getPrivateValue(
                        PacketDistributor::class.java, this, "functor"
                )
        )
    }

    private fun <T> PacketDistributor<T>.setFunctor(newValue: PDFunctor<T>) {
        ObfuscationReflectionHelper.setPrivateValue(
                PacketDistributor::class.java, this, newValue, "functor"
        )
    }

}