package de.take_weiland.mods.cameracraft.api;

import com.google.common.base.Throwables;
import de.take_weiland.mods.cameracraft.api.camera.Viewport;
import de.take_weiland.mods.cameracraft.api.camera.ViewportProvider;
import de.take_weiland.mods.cameracraft.api.camera.ViewportProviderFactory;
import de.take_weiland.mods.cameracraft.api.energy.BatteryHandler;
import de.take_weiland.mods.cameracraft.api.img.ImageFilter;
import de.take_weiland.mods.cameracraft.api.photo.PhotoDatabase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.concurrent.CompletionStage;

/**
 * <p>Main access interface into the CameraCraft API.</p>
 * <p>Any registration methods must, unless otherwise noted, be called from within a mod loading event and must happen
 * before post init.</p>
 */
public interface CameraCraftApi {

    CompletionStage<Long> defaultTakePhoto(EntityPlayer player, ImageFilter filter);

    BatteryHandler findBatteryHandler(ItemStack stack);

    void registerBatteryHandler(Item battery, BatteryHandler handler);

    default CompletionStage<Long> takePhoto(Viewport viewport) {
        return takePhoto(viewport, null);
    }

    CompletionStage<Long> takePhoto(Viewport viewport, ImageFilter filter);

    /**
     * <p>Get the current {@code PhotoDatabase}. The database remains valid until a server restart. The database is
     * initialized during the initial load of the over world dimension.</p>
     * <p>This method will return null if no server is active, but this is not a guarantee, the database should only be
     * accessed when it is known that a server is active.</p>
     *
     * @return the current PhotoDatabase
     */
    PhotoDatabase getDatabase();

    /**
     * <p>Get a provider for the given viewport using the registered ViewportProviderFactories.</p>
     *
     * @param viewport the viewport
     * @return the provider
     * @throws UnsupportedOperationException if no registered factory can support the given viewport
     */
    ViewportProvider getProvider(Viewport viewport);

    /**
     * <p>Register a new ViewportProviderFactory under the given name.</p>
     * <p>Which factories are used in which order can be changed in the config.</p>
     *
     * @param name    the name, must be unique per mod and not start with character '-'
     * @param factory the factory
     */
    void registerViewportProviderFactory(String name, ViewportProviderFactory factory);

    static CameraCraftApi get() {
        try {
            return (CameraCraftApi) ApiAccessor.apiGet.invokeExact();
        } catch (Throwable x) {
            throw Throwables.propagate(x);
        }
    }

}
