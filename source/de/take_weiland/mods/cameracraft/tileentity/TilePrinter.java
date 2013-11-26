package de.take_weiland.mods.cameracraft.tileentity;

import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.google.common.collect.Lists;

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
	
	private int selectedX, selectedY, selectedZ;
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		if (network == null) {
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
	public String toString() {
		return "TilePrinter [xCoord=" + xCoord + ", yCoord=" + yCoord + ", zCoord=" + zCoord + "]";
	}

	@Override
	public DataNetwork getNetwork() {
		return network;
	}

	@Override
	public boolean hasNetwork() {
		return network != null && network.isValid();
	}

	private List<NetworkNode.ChangeListener> listeners;
	
	@Override
	public void setNetwork(DataNetwork network) {
		DataNetwork oldNetwork = this.network;
		this.network = network;
		if (listeners != null) {
			int l = listeners.size();
			for (int i = 0; i < l; ++i) {
				listeners.get(i).onNewNetwork(this, oldNetwork);
			}
		}
	}

	
	@Override
	public void onNetworkChange() {
		if (listeners != null) {
			int l = listeners.size();
			for (int i = 0; i < l; ++i) {
				listeners.get(i).onNetworkChange(this);
			}
		}
	}

	@Override
	public void addListener(ChangeListener listener) {
		(listeners == null ? (listeners = Lists.newArrayListWithCapacity(3)) : listeners).add(listener);
	}

	@Override
	public void removeListener(ChangeListener listener) {
		if (listeners != null) {
			listeners.remove(listener);
		}
	}

	@Override
	public String getDisplayName() {
		return "Printer [x=" + xCoord + ", y=" + yCoord + ", z=" + zCoord + "]";
	}

}
