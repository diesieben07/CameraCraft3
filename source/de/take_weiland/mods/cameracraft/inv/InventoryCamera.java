package de.take_weiland.mods.cameracraft.inv;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import de.take_weiland.mods.cameracraft.api.camera.Camera;
import de.take_weiland.mods.cameracraft.api.photo.ItemPhotoStorage;
import de.take_weiland.mods.cameracraft.api.photo.PhotoStorage;
import de.take_weiland.mods.cameracraft.item.CameraType;
import de.take_weiland.mods.commons.templates.ItemInventory;
import de.take_weiland.mods.commons.util.Multitypes;

public abstract class InventoryCamera extends ItemInventory.WithPlayer implements Camera, PhotoStorage.Listener {

	private static final String NBT_KEY = "cameracraft.camerainv";
	
	protected InventoryCamera(EntityPlayer player) {
		super(player, NBT_KEY);
	}
	
	@Override
	public abstract CameraType getType();
	
	protected abstract int storageSlot();
	
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
		if (slot == 0) {
			return false; // TODO lenses
		} else {
			return getType().isItemValid(slot, item);
		}
	}
	
	@Override
	public PhotoStorage getPhotoStorage() {
		ItemStack storageSlot = storage[storageSlot()];
		if (storageSlot == null) {
			return null;
		} else {
			Item item = storageSlot.getItem();
			if (item instanceof ItemPhotoStorage) {
				PhotoStorage storage = ((ItemPhotoStorage)item).getStorage(storageSlot);
				storage.addListener(this);
				return storage;
			} else {
				return null;
			}
		}
	}
	
	public boolean hasStorageItem() {
		ItemStack storageSlot = storage[storageSlot()];
		return storageSlot != null && storageSlot.getItem() instanceof ItemPhotoStorage;
	}

	@Override
	public void onChange(PhotoStorage storage) {
		System.out.println("saving");
		saveData();
	}
	
}
