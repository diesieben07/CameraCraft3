package de.take_weiland.mods.cameracraft.inv;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import de.take_weiland.mods.cameracraft.api.camera.Camera;
import de.take_weiland.mods.cameracraft.api.camera.ItemPhotoStorage;
import de.take_weiland.mods.cameracraft.api.camera.PhotoStorage;
import de.take_weiland.mods.cameracraft.item.CameraType;
import de.take_weiland.mods.cameracraft.photo.PhotoStorages;
import de.take_weiland.mods.commons.templates.ItemInventory;
import de.take_weiland.mods.commons.util.Multitypes;

public abstract class InventoryCamera extends ItemInventory.WithPlayer implements Camera {

	private static final String NBT_KEY = "cameracraft.camerainv";
	
	protected InventoryCamera(EntityPlayer player) {
		super(player, NBT_KEY);
	}
	
	@Override
	public abstract CameraType getType();
	
	@Override
	public String getInvName() {
		return Multitypes.name(getType());
	}

	@Override
	public boolean isInvNameLocalized() {
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack item) {
		switch (slot) {
		case 0:
			return item.getItem() instanceof ItemPhotoStorage;
		default:
			return true;
		}
	}

	@Override
	public PhotoStorage getPhotoStorage() {
		ItemStack slot0 = storage[0];
		if (slot0 == null) {
			return null;
		} else {
			Item item = slot0.getItem();
			if (item instanceof ItemPhotoStorage) {
				return ((ItemPhotoStorage)item).getStorage(slot0);
			} else {
				return null;
			}
		}
	}
	
}
