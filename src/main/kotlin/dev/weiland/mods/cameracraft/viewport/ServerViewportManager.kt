package dev.weiland.mods.cameracraft.viewport

import dev.weiland.mods.cameracraft.CameraCraft
import dev.weiland.mods.cameracraft.util.registerInternalCapability
import net.minecraft.entity.Entity
import net.minecraft.network.IPacket
import net.minecraft.util.Direction
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.ChunkPos
import net.minecraft.world.World
import net.minecraft.world.server.ServerWorld
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.CapabilityInject
import net.minecraftforge.common.capabilities.ICapabilityProvider
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.event.AttachCapabilitiesEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent

@Mod.EventBusSubscriber(modid = CameraCraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
internal class ServerViewportManager(private val world: ServerWorld) : ICapabilityProvider {

    private val instances = HashMap<ChunkPos, Instance>()

    class Instance {

        val viewports = ArrayList<Viewport>()

    }

    fun sendToTracking(chunk: ChunkPos, packet: IPacket<*>, boundaryOnly: Boolean) {

    }

    fun sendToTracking(entity: Entity, packet: IPacket<*>) {

    }

    private val lazyOptional = LazyOptional.of { this }

    override fun <T : Any?> getCapability(cap: Capability<T>, side: Direction?): LazyOptional<T> {
        return CAPABILITY.orEmpty(cap, lazyOptional)
    }

    companion object {

        @set:CapabilityInject(ServerViewportManager::class)
        lateinit var CAPABILITY: Capability<ServerViewportManager>

        @JvmStatic
        @SubscribeEvent
        fun startup(event: FMLCommonSetupEvent) {
            registerInternalCapability<ServerViewportManager>()
        }

        @JvmStatic
        fun get(world: ServerWorld): ServerViewportManager {
            return world.getCapability(CAPABILITY).orElseThrow { IllegalStateException("SeverViewportManager missing") }
        }
    }

    @Mod.EventBusSubscriber(modid = CameraCraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    object AttachmentEventHandler {

        @JvmStatic
        @SubscribeEvent
        fun attachCapabilities(evt: AttachCapabilitiesEvent<World>) {
            val world = evt.`object`
            if (world is ServerWorld) {
                evt.addCapability(ResourceLocation(CameraCraft.MOD_ID, "viewport_manager"), ServerViewportManager(world))
            }
        }

    }

}