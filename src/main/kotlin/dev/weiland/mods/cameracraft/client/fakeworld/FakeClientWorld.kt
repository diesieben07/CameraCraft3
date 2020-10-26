package dev.weiland.mods.cameracraft.client.fakeworld

import net.minecraft.client.network.play.ClientPlayNetHandler
import net.minecraft.client.renderer.WorldRenderer
import net.minecraft.client.world.ClientWorld
import net.minecraft.profiler.IProfiler
import net.minecraft.util.RegistryKey
import net.minecraft.world.DimensionType
import net.minecraft.world.World
import java.util.function.Supplier

internal class FakeClientWorld(
    netHandler: ClientPlayNetHandler, worldInfo: ClientWorldInfo, registryKey: RegistryKey<World>, dimensionType: DimensionType, viewDistance: Int,
    profiler: Supplier<IProfiler>, worldRenderer: WorldRenderer, debug: Boolean, seed: Long
) : ClientWorld(netHandler, worldInfo, registryKey, dimensionType, viewDistance, profiler, worldRenderer, debug, seed) {


}