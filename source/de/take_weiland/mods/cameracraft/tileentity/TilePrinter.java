package de.take_weiland.mods.cameracraft.tileentity;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import de.take_weiland.mods.cameracraft.api.cable.NetworkNode;
import de.take_weiland.mods.cameracraft.api.cable.NetworkTile;
import de.take_weiland.mods.cameracraft.blocks.MachineType;
import de.take_weiland.mods.cameracraft.item.CCItem;
import de.take_weiland.mods.cameracraft.networking.NetworkNodeImpl;
import de.take_weiland.mods.cameracraft.networking.NetworkUtil;
import de.take_weiland.mods.commons.templates.TileEntityInventory;
import de.take_weiland.mods.commons.util.ItemStacks;
import de.take_weiland.mods.commons.util.Multitypes;

public class TilePrinter extends TileEntityInventory<TilePrinter> implements NetworkTile {

	public static final int SLOT_YELLOW = 0;
	public static final int SLOT_CYAN = 1;
	public static final int SLOT_MAGENTA = 2;
	public static final int SLOT_BLACK = 3;
	public static final int SLOT_PAPER = 4;
	
	private NetworkNode node = new NetworkNodeImpl(this);
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		if (!node.hasNetwork()) {
			NetworkUtil.initializeNetworking(this);
		}
	}

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
	public void invalidate() {
		super.invalidate();
		NetworkUtil.shutdownNetworking(this);
	}

	@Override
	public String getDisplayName() {
		return "Printer [x=" + xCoord + ", y=" + yCoord + ", z=" + zCoord + "]";
	}

	@Override
	public NetworkNode getNode() {
		return node;
	}

}
