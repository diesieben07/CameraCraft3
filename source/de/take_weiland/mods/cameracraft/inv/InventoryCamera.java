package de.take_weiland.mods.cameracraft.inv;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import de.take_weiland.mods.cameracraft.api.camera.Camera;
import de.take_weiland.mods.cameracraft.api.photo.ItemPhotoStorage;
import de.take_weiland.mods.cameracraft.api.photo.PhotoStorage;
import de.take_weiland.mods.cameracraft.item.CameraType;
import de.take_weiland.mods.commons.templates.ItemInventory;
import de.take_weiland.mods.commons.util.Multitypes;

public abstract class InventoryCamera extends ItemInventory.WithPlayer implements Camera, PhotoStorage.Listener {

	private static final String NBT_KEY = "cameracraft.camerainv";
	private boolean isLidClosed;
	
	protected InventoryCamera(EntityPlayer player) {
		super(player, NBT_KEY);
	}
	
	@Override
	public abstract CameraType getType();
	
	public abstract int storageSlot();
	
	protected abstract boolean canLidClose();
	
	public boolean isLidClosed() {
		return isLidClosed;
	}
	
	public void toggleLid() {
		if (canLidClose()) {
			isLidClosed = !isLidClosed;
			
			if (isLidClosed) {
				closeLid();
			} else {
				openLid();
			}
					
			onChange();
		}
	}
	
	private void closeLid() {
		
	}
	
	private void openLid() {
		PhotoStorage storage = getPhotoStorage();
		if (storage != null) {
			listening = false; // stop us from saving twice
			storage.clear();
			listening = true;
		}
	}
	
	@Override
	protected void writeToNbt(NBTTagCompound nbt) {
		super.writeToNbt(nbt);
		if (canLidClose()) {
			nbt.setBoolean("lid", isLidClosed);
		}
	}

	@Override
	protected void readFromNbt(NBTTagCompound nbt) {
		super.readFromNbt(nbt);
		if (canLidClose()) {
			isLidClosed = nbt.getBoolean("lid");
		}
	}

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
	
	private ItemStack lastStorageStack;
	private PhotoStorage lastStorage;
	
	@Override
	public PhotoStorage getPhotoStorage() {
		ItemStack storageSlot = storage[storageSlot()];
		if (storageSlot == lastStorageStack) {
			return lastStorage;
		} else {
			if (lastStorage != null) {
				lastStorage.removeListener(this);
			}
			lastStorageStack = storageSlot;
			if (storageSlot == null) {
				lastStorage = null;
			} else {
				Item item = storageSlot.getItem();
				if (item instanceof ItemPhotoStorage) {
					PhotoStorage storage = ((ItemPhotoStorage)item).getStorage(storageSlot);
					storage.addListener(this);
					lastStorage = storage;
				} else {
					lastStorage = null;
				}
			}
			return lastStorage;
		}
	}
	
	@Override
	public void closeChest() {
		super.closeChest();
		if (lastStorage != null) {
			lastStorage.removeListener(this);
		}
	}

	public boolean hasStorageItem() {
		ItemStack storageSlot = storage[storageSlot()];
		return storageSlot != null && storageSlot.getItem() instanceof ItemPhotoStorage;
	}

	private boolean listening = true;
	
	@Override
	public void onChange(PhotoStorage storage) {
		if (listening) {
			saveData();
		}
	}
	
}
