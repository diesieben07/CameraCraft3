package de.take_weiland.mods.cameracraft.api;

import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.cameracraft.api.energy.BatteryHandler;
import de.take_weiland.mods.cameracraft.api.img.ImageFilter;
import de.take_weiland.mods.cameracraft.api.photo.PhotoDatabase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable;

import java.util.concurrent.CompletionStage;

public interface CameraCraftApi {

    GenerateMinable.EventType getTinMinableType();

    GenerateMinable.EventType getAlkalineMinableType();

    GenerateMinable.EventType getPhotonicMinableType();

    CompletionStage<Long> defaultTakePhoto(EntityPlayer player, ImageFilter filter);

    BatteryHandler findBatteryHandler(ItemStack stack);

    void registerBatteryHandler(Item battery, BatteryHandler handler);

    /**
     * <p>Obtain the current {@code PhotoDatabase}. The database is valid until a server restart. The database is
     * initialized during the initial load of the overworld.</p>
     * <p>This method will return null if no server is active, but this is not a guarantee, the database should only be
     * accessed when it is known that a server is active.</p>
     *
     * @return the current PhotoDatabase
     */
    PhotoDatabase getDatabase();

    static CameraCraftApi get() {
        return CameraCraft.api;
    }

}
