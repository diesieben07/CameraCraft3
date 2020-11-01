package dev.weiland.mods.cameracraft.viewport

import net.minecraft.entity.Entity
import java.lang.ref.PhantomReference
import java.lang.ref.ReferenceQueue
import java.lang.ref.WeakReference
import kotlin.concurrent.thread

internal class Viewport(
) {



    private fun cleanup() {

    }

    companion object {

//        private val ownerQueue = ReferenceQueue<Any>()
//
//        init {
//            thread(isDaemon = true) {
//                while (true) {
//                    val ref = ownerQueue.remove()
//                    (ref as Viewport).cleanup()
//                }
//            }
//        }
//
//        fun createForEntity(entity: Entity): Viewport {
//            TODO()
//        }
    }

}