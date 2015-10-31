package de.take_weiland.mods.cameracraft.tileentity;

import de.take_weiland.mods.cameracraft.api.PhotoStorageProvider;
import de.take_weiland.mods.cameracraft.api.photo.PhotoStorage;
import de.take_weiland.mods.cameracraft.api.photo.PhotoStorageItem;
import de.take_weiland.mods.cameracraft.blocks.CCBlock;
import de.take_weiland.mods.cameracraft.blocks.MachineType;
import de.take_weiland.mods.commons.meta.HasSubtypes;
import de.take_weiland.mods.commons.tileentity.TileEntityInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class TileScanner extends TileEntityInventory implements PhotoStorageProvider {

    public static final int SLOT_SOURCE = 0;
    public static final int SLOT_TARGET = 1;

    private static final int WAITING = -1;
    private static final int TIME_PER_PHOTO = 30;

    private int counter = WAITING;

    public void requestScan() {
        if (counter == WAITING) {
            counter = getScanDuration();
        }
    }

    @Override
    public void updateEntity() {
        if (counter > 0) {
            counter--;
        } else if (counter == 0) {
            doScan();
            counter = WAITING;
        }
    }

    private void doScan() {
        PhotoStorage source = getSource();
        PhotoStorage target = getTarget();

        if (source != null && target != null) {
            for (long photoId : source) {
                target.store(photoId);
            }
        }
    }

    private int getScanDuration() {
        ItemStack source = storage[SLOT_SOURCE];
        ItemStack target = storage[SLOT_TARGET];
        if (source == null || !(source.getItem() instanceof PhotoStorageItem) || !((PhotoStorageItem) source.getItem()).canBeScanned(source)) {
            return WAITING;
        } else if (target == null || !(target.getItem() instanceof PhotoStorageItem) || !((PhotoStorageItem) target.getItem()).isRandomAccess(target)) {
            return WAITING;
        } else {
            return ((PhotoStorageItem) target.getItem()).getPhotoStorage(target).size() * TIME_PER_PHOTO;
        }
    }

    private PhotoStorage getSource() {
        return getStorage(storage[SLOT_SOURCE]);
    }

    private PhotoStorage getTarget() {
        return getStorage(storage[SLOT_TARGET]);
    }

    private PhotoStorage getStorage(ItemStack stack) {
        Item item;
        if (stack == null || !((item = stack.getItem()) instanceof PhotoStorageItem)) {
            return null;
        } else {
            return ((PhotoStorageItem) item).getPhotoStorage(stack);
        }
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
