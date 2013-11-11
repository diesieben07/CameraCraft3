package de.take_weiland.mods.cameracraft.tileentity;

import net.minecraft.item.ItemStack;
import de.take_weiland.mods.cameracraft.blocks.MachineType;
import de.take_weiland.mods.cameracraft.item.PhotoStorageType;
import de.take_weiland.mods.commons.templates.NameableTileEntity;
import de.take_weiland.mods.commons.templates.TileEntityInventory;
import de.take_weiland.mods.commons.util.ItemStacks;
import de.take_weiland.mods.commons.util.Multitypes;

public class TileCardReader extends TileEntityInventory<TileCardReader> implements NameableTileEntity {

	public static final int NO_ACC = 0;
	public static final int READ_ACC = 1;
	public static final int WRITE_ACC = 2;
	
	private int access = NO_ACC;
	
	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	protected String getDefaultName() {
		return Multitypes.fullName(MachineType.CARD_READER);
	}

	public int getAccessState() {
		return access;
	}

	public void setAccessState(int access) {
		this.access = access;
	}

	@Override
	public void updateEntity() {
		if (storage[0] != null) {
			access = READ_ACC;
		} else {
			access = NO_ACC;
		}
	}
	
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack item) {
		return slot == 0 && ItemStacks.is(item, PhotoStorageType.MEMORY_CARD);
	}

}
