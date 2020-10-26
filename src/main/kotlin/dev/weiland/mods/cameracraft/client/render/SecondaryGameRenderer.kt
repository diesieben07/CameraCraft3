package dev.weiland.mods.cameracraft.client.render

import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.systems.RenderSystem
import dev.weiland.mods.cameracraft.CameraCraft
import dev.weiland.mods.cameracraft.client.fakeworld.FakeClientWorld
import net.minecraft.block.Blocks
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.LightTexture
import net.minecraft.client.renderer.WorldRenderer
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
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.BiomeContainer
import net.minecraft.world.biome.Biomes
import net.minecraft.world.biome.provider.BiomeProvider
import net.minecraft.world.biome.provider.SingleBiomeProvider
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.chunk.ChunkPrimer
import net.minecraft.world.chunk.EmptyChunk
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

    val frameBuffer: Framebuffer = Framebuffer(imageWidth, imageHeight, true, Minecraft.IS_RUNNING_ON_MAC)

    val globalRenderState = GlobalRenderState(
        viewBobbing = false,
        pointOfView = PointOfView.FIRST_PERSON,
        viewportEntity = entity,
        mainFramebufferWidth = imageWidth,
        mainFramebufferHeight = imageHeight,
        mainFramebuffer = frameBuffer,
        activeRenderPrevHeight = entity.eyeHeight,
        activeRenderHeight = entity.eyeHeight
    )

    val worldRenderer = WorldRenderer(mc, mc.renderTypeBuffers)
    var fakeWorld: FakeClientWorld? = null
    val lightmap = LightTexture(mc.gameRenderer, mc).also { it.tick() }
    var first = true

    fun preRender(): Boolean {
        if (!entity.isAlive) {
            return false
        }

        val savedRenderState = GlobalRenderState.capture(mc)
        val savedWR = mc.worldRenderer
        val savedWorld = mc.world
        val savedLightmap = mc.gameRenderer.lightmapTexture

        globalRenderState.restore(mc)
        mc.worldRenderer = worldRenderer
        mc.gameRenderer.lightmapTexture = lightmap
        lightmap.tick()

        if (fakeWorld == null) {
            fakeWorld = FakeClientWorld(
                mc.connection!!, ClientWorld.ClientWorldInfo(Difficulty.EASY, false, false),
                mc.world!!.dimensionKey, mc.world!!.dimensionType,
                3, // TODO,
                { mc.profiler },
                worldRenderer,
                false,
                0L
            ).also { fw ->
                fw.worldInfo.dayTime = 6000L


                mc.worldRenderer.setWorldAndLoadRenderers(fw)

                val pos = entity.position.up().south(5)

                val chunkPos = ChunkPos(pos)
                fw.chunkProvider.setCenter(chunkPos.x, chunkPos.z)

                val chunkPrimer = ChunkPrimer(chunkPos, UpgradeData.EMPTY)
                chunkPrimer.setBlockState(pos, Blocks.REDSTONE_BLOCK.defaultState, false)
                chunkPrimer.biomes = BiomeContainer(
                    fw.func_241828_r().getRegistry(Registry.BIOME_KEY),
                    chunkPos,
                    SingleBiomeProvider(fw.func_241828_r().getRegistry(Registry.BIOME_KEY).getValueForKey(Biomes.PLAINS))
                )

                val chunk = Chunk(fw, chunkPrimer)
                val packet = SChunkDataPacket(chunk, 65535)
                fw.chunkProvider.loadChunk(
                    chunkPos.x, chunkPos.z, chunk.biomes,
                    packet.readBuffer, packet.heightmapTags, packet.availableSections, packet.isFullChunk
                )
            }
        }
        mc.world = fakeWorld

        mc.framebuffer.bindFramebuffer(true)

        RenderSystem.pushMatrix()
        inFakeRender = true
        mc.gameRenderer.renderWorld(if (mc.isGamePaused) mc.renderPartialTicksPaused else mc.renderPartialTicks, Util.nanoTime(), MatrixStack())
        inFakeRender = false
        RenderSystem.popMatrix()

        savedRenderState.restore(mc)
        mc.worldRenderer = savedWR
        mc.world = savedWorld
        mc.gameRenderer.lightmapTexture = savedLightmap

        mc.framebuffer.bindFramebuffer(true)

        return true
    }

    fun render() {
        RenderSystem.pushMatrix()
        frameBuffer.framebufferRender(256, 256)
        RenderSystem.popMatrix()
    }

    companion object {

        @JvmField
        var inFakeRender = false

        private var active: Array<SecondaryGameRenderer> = emptyArray()

        private var current: SecondaryGameRenderer? = null

        @JvmStatic
        @SubscribeEvent
        fun renderTick(evt: TickEvent.RenderTickEvent) {
            if (Minecraft.getInstance().world == null || Minecraft.getInstance().playerController == null || current?.entity?.isAlive == false) {
                current?.frameBuffer?.deleteFramebuffer()
                current = null
            }
            if (evt.phase == TickEvent.Phase.END) {
                current?.render()
            } else {
                current?.preRender()
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
            if (inFakeRender) {
                evt.fov = 70.0
            }
        }

        @JvmStatic
        @SubscribeEvent(priority = EventPriority.LOWEST)
        fun preScreenRender(evt: GuiScreenEvent.DrawScreenEvent.Pre) {
            if (inFakeRender) {
                evt.isCanceled = true
            }
        }

        fun createOverlayForEntity(entity: LivingEntity) {
            current?.frameBuffer?.deleteFramebuffer()
            current = SecondaryGameRenderer(
                Minecraft.getInstance(),
                entity
            )
        }

    }

}
