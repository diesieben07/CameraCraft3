package de.take_weiland.mods.cameracraft.inv;

import java.util.List;
import java.util.concurrent.ExecutionException;

import net.minecraft.crash.CrashReport;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ReportedException;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import de.take_weiland.mods.cameracraft.api.camera.Camera;
import de.take_weiland.mods.cameracraft.api.camera.Lens;
import de.take_weiland.mods.cameracraft.api.img.ImageFilter;
import de.take_weiland.mods.cameracraft.api.photo.ItemPhotoStorage;
import de.take_weiland.mods.cameracraft.api.photo.PhotoStorage;
import de.take_weiland.mods.cameracraft.img.ImageFilters;
import de.take_weiland.mods.cameracraft.item.CameraType;
import de.take_weiland.mods.cameracraft.photo.PhotoManager;
import de.take_weiland.mods.commons.templates.ItemInventory;
import de.take_weiland.mods.commons.util.Multitypes;
import de.take_weiland.mods.commons.util.Sides;

public abstract class InventoryCamera extends ItemInventory.WithPlayer implements Camera, PhotoStorage.Listener {

	private static final int LENS_SLOT = 0;
	private static final String NBT_KEY = "cameracraft.camerainv";
	
	private boolean isLidClosed;
	
	protected InventoryCamera(EntityPlayer player) {
		super(player, NBT_KEY);
	}
	
	public Lens getLens() {
		ItemStack lens = storage[LENS_SLOT];
		if (lens == null) {
			return null;
		} else {
			Item item = lens.getItem();
			return item instanceof Lens ? (Lens)item : null;
		}
	}
	
	public ImageFilter getLensFilter() {
		Lens lens = getLens();
		return lens == null ? null : lens.getFilter(storage[LENS_SLOT]);
	}
	
	public ItemStack getLensStack() {
		return storage[LENS_SLOT];
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
	
	private List<ListenableFuture<Void>> convertTasks;
	
	private void openLid() {
		if (Sides.logical(player).isClient()) {
			return;
		}
		PhotoStorage storage = getPhotoStorage();
		if (storage != null) {
			waitForRemainingTasks();
			convertTasks = PhotoManager.applyFilterTo(storage, ImageFilters.OVEREXPOSE);
		}
	}
	
	private void waitForRemainingTasks() {
		if (convertTasks != null) {
			ListenableFuture<List<Void>> combined = Futures.allAsList(convertTasks);
			try {
				long start = System.currentTimeMillis();
				List<Void> result = combined.get();
				System.out.println("spent " + (System.currentTimeMillis() - start) + " ms cleaning up " + result.size() + " tasks");
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			} catch (ExecutionException e) {
				CrashReport cr = CrashReport.makeCrashReport(e, "Applying filters to CameraCraft photos");
				throw new ReportedException(cr);
			}
			convertTasks = null;
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
		if (slot == LENS_SLOT) {
			return item.getItem() instanceof Lens;
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
		waitForRemainingTasks();
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
