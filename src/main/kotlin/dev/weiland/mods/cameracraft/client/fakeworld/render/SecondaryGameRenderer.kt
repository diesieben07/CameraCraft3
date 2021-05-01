package dev.weiland.mods.cameracraft.client.fakeworld.render

import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.systems.RenderSystem
import dev.weiland.mods.cameracraft.CameraCraft
import dev.weiland.mods.cameracraft.client.fakeworld.FakeClientWorld
import net.minecraft.block.Blocks
import net.minecraft.client.Minecraft
import net.minecraft.client.settings.PointOfView
import net.minecraft.client.shader.Framebuffer
import net.minecraft.client.world.ClientWorld
import net.minecraft.entity.LivingEntity
import net.minecraft.network.play.server.SChunkDataPacket
import net.minecraft.util.Util
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.palette.UpgradeData
import net.minecraft.util.registry.Registry
import net.minecraft.world.Difficulty
import net.minecraft.world.biome.BiomeContainer
import net.minecraft.world.biome.Biomes
import net.minecraft.world.biome.provider.SingleBiomeProvider
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.chunk.ChunkPrimer
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.EntityViewRenderEvent
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.EventPriority
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

@Mod.EventBusSubscriber(Dist.CLIENT, modid = CameraCraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
internal class SecondaryGameRenderer(private val mc: Minecraft, val entity: LivingEntity) {

    val imageWidth = /*mc.mainWindow.framebufferWidth*/ 256
    val imageHeight = /*mc.mainWindow.framebufferHeight*/ 256

    val frameBuffer: Framebuffer = Framebuffer(imageWidth, imageHeight, true, Minecraft.ON_OSX)

    val globalRenderState = GlobalRenderState(
//            viewBobbing = false,
            pointOfView = PointOfView.FIRST_PERSON,
            viewportEntity = entity,
//            mainFramebufferWidth = imageWidth,
//            mainFramebufferHeight = imageHeight,
    )

    val gameRenderer = FakeGameRenderer(mc, mc.resourceManager, mc.renderBuffers(), imageWidth.toFloat(), imageHeight.toFloat(), entity)
    val worldRenderer = FakeWorldRenderer(mc, mc.renderBuffers(), frameBuffer)
    var fakeWorld: FakeClientWorld? = null
    var first = true

    fun preRender(): Boolean {
        if (!entity.isAlive) {
            return false
        }

        val savedRenderState = GlobalRenderState.capture(mc)
        val savedWR = mc.levelRenderer
        val savedWorld = mc.level
        val savedGameRenderer = mc.gameRenderer

        globalRenderState.restore(mc)
        mc.levelRenderer = worldRenderer
        mc.gameRenderer = gameRenderer

        if (fakeWorld == null) {
            fakeWorld = FakeClientWorld(
                    mc.connection!!, ClientWorld.ClientWorldInfo(Difficulty.EASY, false, false),
                    mc.level!!.dimension(), mc.level!!.dimensionType(),
                    3, // TODO,
                    { mc.profiler },
                    worldRenderer,
                    false,
                    0L
            ).also { fw ->
                fw.levelData.dayTime = 6000L


                mc.levelRenderer.setLevel(fw)

                val pos = entity.blockPosition().above().south(5)

                val chunkPos = ChunkPos(pos)
                fw.chunkSource.updateViewCenter(chunkPos.x, chunkPos.z)

                val chunkPrimer = ChunkPrimer(chunkPos, UpgradeData.EMPTY)
                chunkPrimer.setBlockState(pos, Blocks.REDSTONE_BLOCK.defaultBlockState(), false)
                chunkPrimer.biomes = BiomeContainer(
                        fw.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY),
                        chunkPos,
                        SingleBiomeProvider(fw.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).get(Biomes.PLAINS))
                )

                val chunk = Chunk(fw, chunkPrimer)
                val packet = SChunkDataPacket(chunk, 65535)
                fw.chunkSource.replaceWithPacketData(
                        chunkPos.x, chunkPos.z, chunk.biomes,
                        packet.readBuffer, packet.heightmaps, packet.availableSections, packet.isFullChunk
                )
            }
        }
        mc.level = fakeWorld

        frameBuffer.bindWrite(true)

        RenderSystem.pushMatrix()
        current = this
        mc.gameRenderer.renderLevel(if (mc.isPaused) mc.pausePartialTick else mc.frameTime, Util.getNanos(), MatrixStack())
        current = null
        RenderSystem.popMatrix()

        savedRenderState.restore(mc)
        mc.levelRenderer = savedWR
        mc.level = savedWorld
        mc.gameRenderer = savedGameRenderer

        mc.mainRenderTarget.bindWrite(true)

        return true
    }

    fun render() {
        RenderSystem.pushMatrix()
        frameBuffer.blitToScreen(256, 256)
        RenderSystem.popMatrix()
    }

    companion object {

        private val active = ArrayList<SecondaryGameRenderer>()

        @JvmField
        var current: SecondaryGameRenderer? = null

        @JvmStatic
        @SubscribeEvent
        fun renderTick(evt: TickEvent.RenderTickEvent) {
            if (evt.phase == TickEvent.Phase.START) {
                if (Minecraft.getInstance().level == null || Minecraft.getInstance().gameMode == null) {
                    for (r in active) {
                        r.frameBuffer.destroyBuffers()
                    }
                    active.clear()
                }

                try {
                    val iterator = active.iterator()
                    for (renderer in iterator) {
                        if (!renderer.preRender()) {
                            iterator.remove()
                        }
                    }
                } finally {
                    current = null
                }
            } else {
                for (renderer in active) {
                    renderer.render()
                }
            }
        }


        @JvmStatic
        @SubscribeEvent
        fun clientTick(evt: TickEvent.ClientTickEvent) {
            if (evt.phase == TickEvent.Phase.START) {
            }
        }

        @JvmStatic
        @SubscribeEvent(priority = EventPriority.LOWEST)
        fun fovModifierEvt(evt: EntityViewRenderEvent.FOVModifier) {
            // Force FOV to default when in a camera render
            if (current != null) {
                evt.fov = 70.0
            }
        }

        @JvmStatic
        @SubscribeEvent(priority = EventPriority.LOWEST)
        fun preScreenRender(evt: GuiScreenEvent.DrawScreenEvent.Pre) {
            if (current != null) {
                evt.isCanceled = true
            }
        }

        fun createOverlayForEntity(entity: LivingEntity) {
            active += SecondaryGameRenderer(
                    Minecraft.getInstance(),
                    entity
            )
        }

    }

}