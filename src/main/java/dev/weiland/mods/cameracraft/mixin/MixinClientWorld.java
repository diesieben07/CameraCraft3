package dev.weiland.mods.cameracraft.mixin;

import dev.weiland.mods.cameracraft.client.fakeworld.ClientWorldInterface;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.profiler.IProfiler;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.storage.ISpawnWorldInfo;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Supplier;

@Mixin(ClientWorld.class)
public abstract class MixinClientWorld extends World implements ClientWorldInterface {

    private MixinClientWorld(ISpawnWorldInfo worldInfo, RegistryKey<World> dimension, DimensionType dimensionType, Supplier<IProfiler> profiler, boolean isRemote, boolean isDebug, long seed) {
        super(worldInfo, dimension, dimensionType, profiler, isRemote, isDebug, seed);
    }

    @Override
    public boolean getCameracraftIsFakeWorld() {
        return false;
    }

    @Redirect(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraftforge/eventbus/api/IEventBus;post(Lnet/minecraftforge/eventbus/api/Event;)Z"
            )
    )
    public boolean postEventHook(IEventBus bus, Event event) {
        if (getCameracraftIsFakeWorld()) {
            return false;
        } else {
            return bus.post(event);
        }
    }

    @Redirect(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/world/ClientWorld;gatherCapabilities()V"
            )
    )
    public void gatherCapabilitiesHook(ClientWorld world) {
        if (!getCameracraftIsFakeWorld()) {
            gatherCapabilities();
        }
    }


}
