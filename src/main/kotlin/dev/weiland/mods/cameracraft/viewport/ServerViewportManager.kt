package dev.weiland.mods.cameracraft.viewport

import dev.weiland.mods.cameracraft.CameraCraft
import dev.weiland.mods.cameracraft.mixin.ChunkManagerAccessor
import dev.weiland.mods.cameracraft.mixin.ChunkManagerEntityTrackerAccessor
import dev.weiland.mods.cameracraft.network.CreateViewportPacket
import dev.weiland.mods.cameracraft.util.registerInternalCapability
import net.minecraft.block.Blocks
import net.minecraft.client.network.play.IClientPlayNetHandler
import net.minecraft.entity.Entity
import net.minecraft.entity.MobEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.network.IPacket
import net.minecraft.network.play.server.SChunkDataPacket
import net.minecraft.network.play.server.SMountEntityPacket
import net.minecraft.network.play.server.SSetPassengersPacket
import net.minecraft.network.play.server.SUpdateLightPacket
import net.minecraft.util.Direction
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.palette.UpgradeData
import net.minecraft.util.registry.Registry
import net.minecraft.world.World
import net.minecraft.world.biome.BiomeContainer
import net.minecraft.world.biome.Biomes
import net.minecraft.world.biome.provider.SingleBiomeProvider
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.chunk.ChunkPrimer
import net.minecraft.world.server.ServerWorld
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.CapabilityInject
import net.minecraftforge.common.capabilities.ICapabilityProvider
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.event.AttachCapabilitiesEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.fml.network.PacketDistributor
import org.apache.logging.log4j.LogManager

@Mod.EventBusSubscriber(modid = CameraCraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
internal class ServerViewportManager(private val world: ServerWorld) : ICapabilityProvider {

    private val instances = HashMap<ChunkPos, Instance>()

    class Instance {

        val viewports = ArrayList<Viewport>()

    }

    fun createViewport(entity: Entity, managingPlayer: ServerPlayerEntity) {
        val vp = Viewport(entity, managingPlayer)
        instances.getOrPut(ChunkPos(entity.xChunk, entity.zChunk), ::Instance).viewports += vp
        sendInitialPackets(vp)
    }

    private fun sendInitialPackets(
        vp: Viewport
    ) {
        val serverWorld = vp.entity.level as ServerWorld
        val chunk = serverWorld.getChunk(vp.entity.xChunk, vp.entity.zChunk)

        CameraCraft.NETWORK.send(
            PacketDistributor.PLAYER.with { vp.managingPlayer },
            CreateViewportPacket(vp.entity.id)
        )

//        val pos = vp.entity.blockPosition().above().south(5)
//        val chunkPos = ChunkPos(pos)
//
//        val chunkPrimer = ChunkPrimer(chunkPos, UpgradeData.EMPTY)
//        chunkPrimer.setBlockState(pos, Blocks.REDSTONE_BLOCK.defaultBlockState(), false)
//        chunkPrimer.biomes = BiomeContainer(
//                vp.entity.level.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY),
//                chunkPos,
//                SingleBiomeProvider(vp.entity.level.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).get(Biomes.PLAINS)!!)
//        )
//
//        val fakeChunk = Chunk(vp.entity.level, chunkPrimer)
//        val packet = SChunkDataPacket(fakeChunk, 65535)
//        vp.sendWrappedPacket(packet)

        vp.sendWrappedPacket(SChunkDataPacket(chunk, 65535))
        vp.sendWrappedPacket(SUpdateLightPacket(chunk.pos, serverWorld.lightEngine, true))

        val entitiesToLink = mutableListOf<MobEntity>()
        val entitiesWithPassengers = mutableListOf<Entity>()

        for (chunkEntitySection in chunk.entitySections) {
            for (entity in chunkEntitySection) {
                val entityTracker = (serverWorld.chunkSource.chunkMap as ChunkManagerAccessor).entityMap[entity.id]
                if (entityTracker == null) {
                    LOGGER.warn("Failed to find entity in ChunkMap!")
                    continue
                }
                val serverEntity = (entityTracker as ChunkManagerEntityTrackerAccessor).serverEntity
                serverEntity.sendPairingData(vp::sendWrappedPacket)
                if (entity is MobEntity && entity.leashHolder != null) {
                    entitiesToLink += entity
                }
                if (entity.passengers.isNotEmpty()) {
                    entitiesWithPassengers.add(entity)
                }
            }
        }
        for (entity in entitiesToLink) {
            vp.sendWrappedPacket(SMountEntityPacket(entity, entity.leashHolder))
        }
        for (entity in entitiesWithPassengers) {
            vp.sendWrappedPacket(SSetPassengersPacket(entity))
        }
    }

    fun sendToTracking(chunk: ChunkPos, packet: IPacket<IClientPlayNetHandler>, boundaryOnly: Boolean) {
        val instance = instances[chunk] ?: return
        for (vp in instance.viewports) {
            vp.sendWrappedPacket(packet)
        }
    }

    fun sendToTracking(entity: Entity, packet: IPacket<IClientPlayNetHandler>) {
        sendToTracking(ChunkPos(entity.xChunk, entity.zChunk), packet, false)
    }

    private val lazyOptional = LazyOptional.of { this }

    override fun <T : Any?> getCapability(cap: Capability<T>, side: Direction?): LazyOptional<T> {
        return CAPABILITY.orEmpty(cap, lazyOptional)
    }

    companion object {

        private val LOGGER = LogManager.getLogger(ServerViewportManager::class.java)

        @set:CapabilityInject(ServerViewportManager::class)
        @set:JvmStatic
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