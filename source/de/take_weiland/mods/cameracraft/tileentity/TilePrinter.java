package de.take_weiland.mods.cameracraft.tileentity;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import de.take_weiland.mods.cameracraft.api.cable.DataNetwork;
import de.take_weiland.mods.cameracraft.api.cable.NetworkNode;
import de.take_weiland.mods.cameracraft.blocks.MachineType;
import de.take_weiland.mods.cameracraft.item.CCItem;
import de.take_weiland.mods.cameracraft.networking.NetworkUtil;
import de.take_weiland.mods.commons.templates.TileEntityInventory;
import de.take_weiland.mods.commons.util.ItemStacks;
import de.take_weiland.mods.commons.util.Multitypes;

public class TilePrinter extends TileEntityInventory<TilePrinter> implements NetworkNode {

	public static final int SLOT_YELLOW = 0;
	public static final int SLOT_CYAN = 1;
	public static final int SLOT_MAGENTA = 2;
	public static final int SLOT_BLACK = 3;
	public static final int SLOT_PAPER = 4;
	
	private DataNetwork network;
	
	public int getSizeInventory() {
		return 5;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack item) {
		if (ItemStacks.is(item, CCItem.miscItems)) {
			return Multitypes.getType(CCItem.miscItems, item).ordinal() - 1 == slot;
		} else {
			return slot == SLOT_PAPER && ItemStacks.is(item, Item.paper);
		}
	}

	@Override
	protected String getDefaultName() {
		return Multitypes.fullName(MachineType.PRINTER);
	}
	
	@Override
	public void validate() {
		super.validate();
		NetworkUtil.initializeNetworking(this);
	}
	
	@Override
	public void invalidate() {
		super.invalidate();
		NetworkUtil.shutdownNetworking(this);
	}

	@Override
	public String toString() {
		return "TilePrinter [xCoord=" + xCoord + ", yCoord=" + yCoord + ", zCoord=" + zCoord + "]";
	}

	@Override
	public DataNetwork getNetwork() {
		return network;
	}

	@Override
	public boolean hasNetwork() {
		return network != null;
	}

	@Override
	public void setNetwork(DataNetwork network) {
		this.network = network;
	}

}
