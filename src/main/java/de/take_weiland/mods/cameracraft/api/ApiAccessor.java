package de.take_weiland.mods.cameracraft.api;

import cpw.mods.fml.common.Loader;
import de.take_weiland.mods.cameracraft.api.energy.BatteryHandler;
import de.take_weiland.mods.cameracraft.api.img.ImageFilter;
import de.take_weiland.mods.cameracraft.api.photo.PhotoDatabase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.terraingen.OreGenEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static java.lang.invoke.MethodType.methodType;

/**
 * @author diesieben07
 */
final class ApiAccessor {

    static final MethodHandle apiGet;

    static {
        if (Loader.isModLoaded("cameracraft")) {
            try {
                Class<?> clazz = Class.forName("de.take_weiland.mods.cameracraft.CameraCraft");
                Field field = clazz.getDeclaredField("api");
                field.setAccessible(true);
                apiGet = MethodHandles.publicLookup().unreflectGetter(field).asType(methodType(CameraCraftApi.class));
            } catch (Throwable x) {
                throw new RuntimeException("Failed to access CameraCraft API field", x);
            }
        } else {
            Logger log = LogManager.getLogger("CameraCraftAPI");
            log.warn("CameraCraftAPI used but CameraCraft not loaded. Please do not put CameraCraftAPI code into your mod!");
            log.warn("Using dummy API implementation");
            apiGet = MethodHandles.constant(CameraCraftApi.class, new DummyApi());
        }
    }

    private static final class DummyApi implements CameraCraftApi {

        @Override
        public OreGenEvent.GenerateMinable.EventType getTinMinableType() {
            return null;
        }

        @Override
        public OreGenEvent.GenerateMinable.EventType getAlkalineMinableType() {
            return null;
        }

        @Override
        public OreGenEvent.GenerateMinable.EventType getPhotonicMinableType() {
            return null;
        }

        @Override
        public CompletionStage<Long> defaultTakePhoto(EntityPlayer player, ImageFilter filter) {
            CompletableFuture<Long> future = new CompletableFuture<>();
            future.completeExceptionally(new Exception("CameraCraft not loaded"));
            return future;
        }

        @Override
        public BatteryHandler findBatteryHandler(ItemStack stack) {
            return null;
        }

        @Override
        public void registerBatteryHandler(Item battery, BatteryHandler handler) {

        }

        @Override
        public PhotoDatabase getDatabase() {
            return null;
        }
    }

}
