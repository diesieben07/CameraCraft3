package de.take_weiland.mods.cameracraft.api;

import com.google.common.util.concurrent.ListenableFuture;
import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.cameracraft.api.energy.BatteryHandler;
import de.take_weiland.mods.cameracraft.api.photo.PhotoDatabase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable;

public interface CameraCraftApi {
	
	GenerateMinable.EventType getTinMinableType();

	GenerateMinable.EventType getAlkalineMinableType();

	GenerateMinable.EventType getPhotonicMinableType();
	
	boolean isCamera(ItemStack stack);
	
	ListenableFuture<String> takePhoto(EntityPlayer viewport);

	BatteryHandler findBatteryHandler(ItemStack stack);
	
	void registerBatteryHandler(Item battery, BatteryHandler handler);

    PhotoDatabase getCurrentPhotoDatabase();

    static CameraCraftApi get() {
        return CameraCraft.api;
    }

}
