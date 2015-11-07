package de.take_weiland.mods.cameracraft.tileentity;

import de.take_weiland.mods.cameracraft.api.PhotoStorageProvider;
import de.take_weiland.mods.cameracraft.api.photo.PhotoStorage;
import de.take_weiland.mods.cameracraft.api.photo.PhotoStorageItem;
import de.take_weiland.mods.cameracraft.blocks.CCBlock;
import de.take_weiland.mods.cameracraft.blocks.MachineType;
import de.take_weiland.mods.commons.meta.HasSubtypes;
import de.take_weiland.mods.commons.sync.Sync;
import de.take_weiland.mods.commons.tileentity.TileEntityInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import static de.take_weiland.mods.commons.util.Sides.sideOf;

public class TileScanner extends TileEntityInventory implements PhotoStorageProvider {

    public static final int SLOT_SOURCE = 0;
    public static final int SLOT_TARGET = 1;

    public static final int TIME_PER_PHOTO = 30;

    private boolean isScanning = false;

    private int photoIndex;

    @Sync(inContainer = true)
    private int scanTimer;

    public void requestScan() {
        if (!isScanning && canScan()) {
            isScanning = true;
            photoIndex = 0;
            scanTimer = TIME_PER_PHOTO;
        }
    }

    public int getPhotoIndex() {
        return photoIndex;
    }

    public int getScanTimer() {
        return scanTimer;
    }

    private boolean canScan() {
        PhotoStorage source = getSource();
        PhotoStorage target = getTarget();

        return source != null && target != null && source.size() > 0 && target.canStore();
    }

    @Override
    public void updateEntity() {
        if (sideOf(this).isServer() && isScanning) {
            if (scanTimer > 0) {
                scanTimer--;
            } else {
                doScan();
                scanTimer = TIME_PER_PHOTO;
            }
        }
    }

    private void doScan() {
        PhotoStorage source = getSource();
        PhotoStorage target = getTarget();

        if (target.store(source.get(photoIndex)) == -1
                || ++photoIndex == source.size()) {
            isScanning = false;
        }
    }

    private ItemStack[] lastStorageStackCache = new ItemStack[2];
    private PhotoStorage[] storageCache = new PhotoStorage[2];

    private PhotoStorage getSource() {
        return getStorage(SLOT_SOURCE);
    }

    private PhotoStorage getTarget() {
        return getStorage(SLOT_TARGET);
    }

    private PhotoStorage getStorage(int slot) {
        ItemStack stack = getStackInSlot(slot);
        ItemStack cached = lastStorageStackCache[slot];
        PhotoStorage storage;

        if (stack != cached) {
            Item item;
            if (stack == null || !((item = stack.getItem()) instanceof PhotoStorageItem)) {
                storage = null;
            } else {
                storage = ((PhotoStorageItem) item).getPhotoStorage(stack);
            }
            storageCache[slot] = storage;
            lastStorageStackCache[slot] = stack;
        } else {
            storage = storageCache[slot];
        }
        return storage;
    }

    @Override
	public int getSizeInventory() {
		return 2;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
        Item item = stack.getItem();
        switch (slot) {
            case SLOT_SOURCE:
                return item instanceof PhotoStorageItem && ((PhotoStorageItem) item).canBeScanned(stack);
            case SLOT_TARGET:
                return item instanceof PhotoStorageItem && ((PhotoStorageItem) item).isRandomAccess(stack);
        }
        throw new IllegalArgumentException();
	}

	@Override
	public String getDefaultName() {
		return HasSubtypes.name(CCBlock.machines, MachineType.SCANNER);
	}

	@Override
	public PhotoStorage getPhotoStorage() {
		ItemStack storage = getPhotoStorageItem();
		return storage == null ? null : ((PhotoStorageItem) storage.getItem()).getPhotoStorage(storage);
	}
	
	private ItemStack getPhotoStorageItem() {
		ItemStack item = storage[0];
		return item != null && item.getItem() instanceof PhotoStorageItem ? item : null;
	}

}
