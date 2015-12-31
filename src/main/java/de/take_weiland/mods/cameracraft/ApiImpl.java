package de.take_weiland.mods.cameracraft;

import com.google.common.collect.Maps;
import de.take_weiland.mods.cameracraft.api.CameraCraftApi;
import de.take_weiland.mods.cameracraft.api.energy.BatteryHandler;
import de.take_weiland.mods.cameracraft.api.img.ImageFilter;
import de.take_weiland.mods.cameracraft.api.photo.PhotoDatabase;
import de.take_weiland.mods.cameracraft.network.PacketRequestStandardPhoto;
import de.take_weiland.mods.cameracraft.worldgen.CCWorldGen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType;

import java.util.Map;
import java.util.concurrent.CompletionStage;

public final class ApiImpl implements CameraCraftApi {

	private final Map<Item, BatteryHandler> batteryHandlers = Maps.newHashMap();
	
	@Override
	public EventType getTinMinableType() {
		return CCWorldGen.TIN;
	}

	@Override
	public EventType getPhotonicMinableType() {
		return CCWorldGen.PHOTONIC;
	}

	@Override
	public EventType getAlkalineMinableType() {
		return CCWorldGen.ALKALINE;
	}

	@Override
	public CompletionStage<Long> defaultTakePhoto(EntityPlayer player, ImageFilter filter) {
		return new PacketRequestStandardPhoto().sendTo(player)
				.thenCompose(packet -> CameraCraft.currentDatabase().saveNewImage(packet.image, filter));
	}

	@Override
	public PhotoDatabase getDatabase() {
		return CameraCraft.currentDatabase();
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

	private enum NullBatteryHandler implements BatteryHandler {
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
