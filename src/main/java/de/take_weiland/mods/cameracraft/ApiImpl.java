package de.take_weiland.mods.cameracraft;

import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ListenableFuture;

import de.take_weiland.mods.cameracraft.api.CameraCraftApi;
import de.take_weiland.mods.cameracraft.api.cable.CableType;
import de.take_weiland.mods.cameracraft.api.camera.CameraItem;
import de.take_weiland.mods.cameracraft.api.energy.BatteryHandler;
import de.take_weiland.mods.cameracraft.blocks.CCBlock;
import de.take_weiland.mods.cameracraft.worldgen.CCWorldGen;
import de.take_weiland.mods.commons.util.Multitypes;

public final class ApiImpl implements CameraCraftApi {

	private final Map<Item, BatteryHandler> batteryHandlers = Maps.newHashMap();
	
	@Override
	public CableType getCableType(IBlockAccess world, int x, int y, int z) {
		return Multitypes.getType(CCBlock.cable, world.getBlockMetadata(x, y, z));
	}

	@Override
	public EventType getTinMinableType() {
		return CCWorldGen.TIN_EVENT_TYPE;
	}

	@Override
	public boolean isCamera(ItemStack stack) {
		return stack != null && stack.getItem() instanceof CameraItem;
	}

	@Override
	public CableType getPowerCable() {
		return de.take_weiland.mods.cameracraft.blocks.CableType.POWER;
	}

	@Override
	public CableType getDataCable() {
		return de.take_weiland.mods.cameracraft.blocks.CableType.DATA;
	}

	@Override
	public ListenableFuture<String> takePhoto(EntityPlayer player) {
		return null;

	}

	@Override
	public BatteryHandler findBatteryHandler(ItemStack battery) {
		Item item = battery.getItem();
		if (item instanceof BatteryHandler) {
			return (BatteryHandler) item;
		}
		BatteryHandler handler = batteryHandlers.get(item);
		if (handler != null) {
			return handler;
		} else {
			return NullBatteryHandler.INSTANCE;
		}
	}
	
	@Override
	public void registerBatteryHandler(Item item, BatteryHandler handler) {
		batteryHandlers.put(item, handler);
	}

	private static enum NullBatteryHandler implements BatteryHandler {
		INSTANCE;

		@Override
		public boolean isBattery(ItemStack stack) {
			return false;
		}
		
		@Override
		public int getCharge(ItemStack stack) {
			return 0;
		}

		@Override
		public boolean isRechargable(ItemStack stack) {
			return false;
		}

		@Override
		public int getCapacity(ItemStack stack) {
			return 0;
		}

		@Override
		public int charge(ItemStack stack, int amount) {
			return 0;
		}

		@Override
		public int drain(ItemStack stack, int amount) {
			return 0;
		}

	}

}
