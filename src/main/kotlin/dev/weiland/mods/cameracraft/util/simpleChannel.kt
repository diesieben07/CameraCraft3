package dev.weiland.mods.cameracraft.util

import net.minecraftforge.fml.network.NetworkDirection
import net.minecraftforge.fml.network.NetworkEvent
import net.minecraftforge.fml.network.simple.SimpleChannel
import java.util.function.BiConsumer
import java.util.function.Supplier

inline fun <reified MSG : Any> SimpleChannel.msg(id: Int, direction: NetworkDirection? = null): SimpleChannel.MessageBuilder<MSG> {
    return messageBuilder(MSG::class.java, id, direction)
}

inline fun <MSG : Any> SimpleChannel.MessageBuilder<MSG>.syncHandler(crossinline consumer: (MSG) -> Unit): SimpleChannel.MessageBuilder<MSG> {
    return syncHandler { msg: MSG, _: NetworkEvent.Context ->
        consumer(msg)
    }
}

inline fun <MSG : Any> SimpleChannel.MessageBuilder<MSG>.syncHandler(crossinline consumer: (MSG, NetworkEvent.Context) -> Unit): SimpleChannel.MessageBuilder<MSG> {
    return handler { msg: MSG, ctx: NetworkEvent.Context ->
        ctx.enqueueWork { consumer(msg, ctx) }
    }
}

inline fun <MSG : Any> SimpleChannel.MessageBuilder<MSG>.handler(crossinline consumer: (MSG, NetworkEvent.Context) -> Unit): SimpleChannel.MessageBuilder<MSG> {
    return this.consumer(BiConsumer<MSG, Supplier<NetworkEvent.Context>> { msg, ctxSupplier ->
        val ctx = ctxSupplier.get()
        ctx.packetHandled = true
        consumer(msg, ctx)
    })
}