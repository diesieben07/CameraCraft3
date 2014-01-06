package de.take_weiland.mods.cameracraft.inv;

import java.util.List;
import java.util.concurrent.ExecutionException;

import net.minecraft.crash.CrashReport;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ReportedException;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import de.take_weiland.mods.cameracraft.CCPlayerData;
import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.cameracraft.api.camera.CameraInventory;
import de.take_weiland.mods.cameracraft.api.camera.LensItem;
import de.take_weiland.mods.cameracraft.api.energy.BatteryHandler;
import de.take_weiland.mods.cameracraft.api.img.ImageFilter;
import de.take_weiland.mods.cameracraft.api.photo.PhotoStorage;
import de.take_weiland.mods.cameracraft.api.photo.PhotoStorageItem;
import de.take_weiland.mods.cameracraft.gui.ContainerCamera;
import de.take_weiland.mods.cameracraft.img.ImageFilters;
import de.take_weiland.mods.cameracraft.item.CameraType;
import de.take_weiland.mods.cameracraft.photo.PhotoManager;
import de.take_weiland.mods.commons.templates.ItemInventory;
import de.take_weiland.mods.commons.util.Listenable;
import de.take_weiland.mods.commons.util.Multitypes;
import de.take_weiland.mods.commons.util.Sides;

public abstract class InventoryCamera extends ItemInventory.WithPlayer<InventoryCamera> implements CameraInventory, Listenable.Listener<PhotoStorage> {

	private static final int COOLDOWN = 30;
	public static final int LENS_SLOT = 0;
	private static final String NBT_KEY = "cameracraft.camerainv";
	
	private boolean isLidClosed;
	
	protected InventoryCamera(EntityPlayer player) {
		super(player, NBT_KEY);
	}
	
	public abstract CameraType getType();
	
	public abstract int storageSlot();
	
	public abstract int batterySlot();
	
	private void closeLid() { }
	
	private List<ListenableFuture<Void>> convertTasks;
	
	private void openLid() {
		if (Sides.logical(player).isClient()) {
			return;
		}
		PhotoStorage storage = getPhotoStorage();
		if (storage != null && !storage.isSealed()) {
			waitForRemainingTasks();
			convertTasks = PhotoManager.applyFilterTo(storage.getPhotos(), ImageFilters.OVEREXPOSE);
		}
	}
	
	@Override
	public void writeToNbt(NBTTagCompound nbt) {
		super.writeToNbt(nbt);
		if (hasLid()) {
			nbt.setBoolean("lid", isLidClosed);
		}
	}

	@Override
	public void readFromNbt(NBTTagCompound nbt) {
		super.readFromNbt(nbt);
		if (hasLid()) {
			isLidClosed = nbt.getBoolean("lid");
		}
	}

	@Override
	public String getInvName() {
		return Multitypes.fullName(getType());
	}

	@Override
	public boolean isInvNameLocalized() {
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack item) {
		if (slot == LENS_SLOT) {
			return item.getItem() instanceof LensItem;
		} else {
			return getType().isItemValid(slot, item);
		}
	}
	
	@Override
	public void closeChest() {
		super.closeChest();
		dispose();
	}

	private boolean listening = true;
	
	@Override
	public void onChange(PhotoStorage storage) {
		if (listening) {
			saveData();
		}
	}
	
	private ItemStack lastStorageStack;
	private PhotoStorage lastStorage;
	
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
	
	// Public API
	
	@Override
	public Container createContainer(EntityPlayer player) {
		return new ContainerCamera(this, player, hasLid() ? storageSlot() : -1);
	}

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
				if (item instanceof PhotoStorageItem) {
					lastStorage = ((PhotoStorageItem)item).getStorage(storageSlot);
					lastStorage.addListener(this);
				} else {
					lastStorage = null;
				}
			}
			return lastStorage;
		}
	}
	
	@Override
	public boolean hasBattery() {
		return getBattery() != null;
	}

	@Override
	public ItemStack getBattery() {
		return needsBattery() ? getStackInSlot(batterySlot()) : null;
	}
	
	private int getBatteryCharge() {
		if (!needsBattery()) {
			return Integer.MAX_VALUE;
		}
		ItemStack b = getBattery();
		if (b == null) {
			return 0;
		}
		BatteryHandler handler = CameraCraft.api.findBatteryHandler(b);
		return handler.getCharge(b);
	}

	@Override
	public ItemStack getCamera() {
		return originalStack.copy();
	}
	
	@Override
	public ItemStack getLens() {
		ItemStack lens = storage[LENS_SLOT];
		return lens == null || !(lens.getItem() instanceof LensItem) ? null : lens;
	}

	@Override
	public boolean hasStorage() {
		ItemStack storageSlot = storage[storageSlot()];
		return storageSlot != null && storageSlot.getItem() instanceof PhotoStorageItem;
	}
	
	@Override
	public ImageFilter getFilter() {
		ItemStack lensStack = getLens();
		LensItem lens = lensStack == null ? null : (LensItem)lensStack.getItem();
		PhotoStorage storage = getPhotoStorage();
		
		ImageFilter lensFilter = lens == null ? null : lens.getFilter(lensStack);
		ImageFilter storageFilter = storage == null ? null : storage.getFilter();
		
		if (hasLid() && !isLidClosed()) {
			return ImageFilters.combine(lensFilter, ImageFilters.OVEREXPOSE, storageFilter);
		} else {
			return ImageFilters.combine(lensFilter, storageFilter);
		}
	}
	
	@Override
	public boolean isLidClosed() {
		return isLidClosed;
	}
	
	@Override
	public void toggleLid() {
		if (hasLid()) {
			isLidClosed = !isLidClosed;
			
			if (isLidClosed) {
				closeLid();
			} else {
				openLid();
			}
					
			onChange();
		}
	}
	
	@Override
	public boolean canTakePhoto() {
		return !CCPlayerData.get(player).isOnCooldown() && getBatteryCharge() > 0 && hasStorage() && getPhotoStorage().canAccept();
	}

	@Override
	public void dispose() {
		if (lastStorage != null) {
			lastStorage.removeListener(this);
		}
		waitForRemainingTasks();
	}

	@Override
	public void onTakePhoto() {
		CCPlayerData.get(player).setCooldown(COOLDOWN);
		ItemStack battery = getBattery();
		if (battery != null) {
			BatteryHandler handler = CameraCraft.api.findBatteryHandler(battery);
			handler.drain(battery, 10);
		}
	}

	@Override
	public void rewind() {
		if (canRewind()) {
			ItemStack film = storage[storageSlot()];
			if (film != null) {
				Item filmItem = film.getItem();
				if (filmItem instanceof PhotoStorageItem) {
					storage[storageSlot()] = ((PhotoStorageItem) filmItem).rewind(film);
					if (isLidClosed) {
						toggleLid();
					}
				}
			}
		}
	}

}
