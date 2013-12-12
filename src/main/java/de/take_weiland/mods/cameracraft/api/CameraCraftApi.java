package de.take_weiland.mods.cameracraft.api;

import com.google.common.util.concurrent.ListenableFuture;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable;
import de.take_weiland.mods.cameracraft.api.cable.CableType;
import de.take_weiland.mods.cameracraft.api.energy.BatteryHandler;

public interface CameraCraftApi {
	
	CableType getCableType(IBlockAccess world, int x, int y, int z);
	
	GenerateMinable.EventType getTinMinableType();
	
	boolean isCamera(ItemStack stack);
	
	CableType getPowerCable();
	
	CableType getDataCable();
	
	ListenableFuture<String> takePhoto(EntityPlayer viewport);
	
	BatteryHandler findBatteryHandler(ItemStack stack);
	
	void registerBatteryHandler(BatteryHandler handler);
	
	boolean isBattery(ItemStack stack);
	
}
