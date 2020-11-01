package dev.weiland.mods.cameracraft.client.fakeworld

import dev.weiland.mods.cameracraft.CameraCraft
import dev.weiland.mods.cameracraft.client.fakeworld.render.FakeWorldRenderer
import net.minecraft.client.Minecraft
import net.minecraft.client.world.ClientWorld
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

    private val worlds: MutableMap<RegistryKey<World>, FakeWorldRenderer> = IdentityHashMap()

    fun load(dimension: RegistryKey<World>, dimensionType: DimensionType) {

        val worldRenderer = FakeWorldRenderer(
                mc, mc.renderTypeBuffers, TODO()
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
        // TODO
        return true
    }

    private fun unapplyContext() {

    }

    inline fun <R : Any> runWithContext(dimension: RegistryKey<World>, handler: () -> R): R? {
        return try {
            if (applyContext(dimension)) {
                handler()
            } else {
                null
            }
        } finally {
            unapplyContext()
        }
    }

    @SubscribeEvent
    @JvmStatic
    fun clientLogout(event: ClientPlayerNetworkEvent.LoggedOutEvent) {

    }

}