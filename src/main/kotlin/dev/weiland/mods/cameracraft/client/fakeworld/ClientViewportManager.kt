package dev.weiland.mods.cameracraft.client.fakeworld

import dev.weiland.mods.cameracraft.CameraCraft
import dev.weiland.mods.cameracraft.client.fakeworld.render.FakeWorldRenderer
import dev.weiland.mods.cameracraft.client.fakeworld.render.SecondaryGameRenderer
import net.minecraft.client.Minecraft
import net.minecraft.client.world.ClientWorld
import net.minecraft.entity.Entity
import net.minecraft.util.RegistryKey
import net.minecraft.world.Difficulty
import net.minecraft.world.DimensionType
import net.minecraft.world.World
import net.minecraftforge.client.event.ClientPlayerNetworkEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import java.util.*

@Mod.EventBusSubscriber(modid = CameraCraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
internal object ClientViewportManager {

    private val mc: Minecraft get() = Minecraft.getInstance()

    private val worlds: MutableMap<RegistryKey<World>, SecondaryGameRenderer> = IdentityHashMap()

    fun createViewport(entity: Entity) {
        worlds.computeIfAbsent(entity.level.dimension()) { dimension ->
            SecondaryGameRenderer(
                mc, entity, dimension, entity.level.dimensionType()
            ).also { SecondaryGameRenderer.addActive(it) }
        }
    }

    fun getOrCreate(dimension: RegistryKey<World>, dimensionType: DimensionType) {

        val worldRenderer = FakeWorldRenderer(
                mc, mc.renderBuffers(), TODO()
        )
        FakeClientWorld(
                mc.connection!!, ClientWorld.ClientWorldInfo(Difficulty.EASY, false, false),
                dimension, dimensionType,
                3, // TODO,
                { mc.profiler },
                worldRenderer,
                false,
                0L
        )
    }

    fun getIfLoaded(dimension: RegistryKey<World>): FakeClientWorld? {
        TODO()
    }

    private fun applyContext(dimension: RegistryKey<World>): Boolean {
        val secondaryGameRenderer = worlds[dimension] ?: return false
        secondaryGameRenderer.pushGlobalState()
        println("apply context $dimension")
        // TODO
        return true
    }

    private fun unapplyContext(dimension: RegistryKey<World>) {
        checkNotNull(worlds[dimension]).popGlobalState()
        println("pop context")
    }

    inline fun <R : Any> runWithContext(dimension: RegistryKey<World>, handler: () -> R): R? {
        return if (applyContext(dimension)) {
            try {
                handler()
            } finally {
                unapplyContext(dimension)
            }
        } else {
            null
        }
    }

    @SubscribeEvent
    @JvmStatic
    fun clientLogout(event: ClientPlayerNetworkEvent.LoggedOutEvent) {

    }

}