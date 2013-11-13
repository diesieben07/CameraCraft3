package de.take_weiland.mods.cameracraft.tileentity;

import java.util.Set;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

import de.take_weiland.mods.cameracraft.api.cable.DataNetwork;
import de.take_weiland.mods.cameracraft.api.cable.NetworkNode;
import de.take_weiland.mods.cameracraft.blocks.MachineType;
import de.take_weiland.mods.cameracraft.item.CCItem;
import de.take_weiland.mods.cameracraft.networking.ChunkloadListener;
import de.take_weiland.mods.cameracraft.networking.ChunkloadingHandler;
import de.take_weiland.mods.cameracraft.networking.NetworkImpl;
import de.take_weiland.mods.commons.templates.TileEntityInventory;
import de.take_weiland.mods.commons.util.ItemStacks;
import de.take_weiland.mods.commons.util.Multitypes;
import de.take_weiland.mods.commons.util.Sides;

public class TilePrinter extends TileEntityInventory<TilePrinter> implements NetworkNode, ChunkloadListener {

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
		if (Sides.logical(this).isServer()) {
			int chunkX = xCoord >> 4;
			int chunkZ = zCoord >> 4;
			if (!worldObj.getChunkProvider().chunkExists(chunkX, chunkZ)) { // don't want to initialize if we're still loading the chunks
				ChunkloadingHandler.get(worldObj).registerListener(chunkX, chunkZ, this);
			} else {
				createOrFindNetwork();
			}
		}
	}
	
	@Override
	public void onChunkLoad() {
		createOrFindNetwork();
	}
	
	private void createOrFindNetwork() {
		Set<DataNetwork> nearbyNetworks = Sets.newHashSetWithExpectedSize(3);
		for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			TileEntity te = worldObj.getBlockTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);
			if (te instanceof NetworkNode && ((NetworkNode)te).hasNetwork()) {
				nearbyNetworks.add(((NetworkNode) te).getNetwork());
			}
		}
		switch (nearbyNetworks.size()) {
		case 0: // we are alone, yay!
			network = new NetworkImpl();
			break;
		case 1: // we have a network nearby, join it
			network = Iterables.getOnlyElement(nearbyNetworks);
			break;
		default: // join two or more networks
			DataNetwork first = null;
			for (DataNetwork network : nearbyNetworks) {
				if (first == null) { // yay, ugly hack because Sets don't guarantee iteration order...
					first = network;
				} else {
					first.joinWith(network);
				}
			}
			network = first;
			break;
		}
		
		network.join(this);
	}

	@Override
	public void invalidate() {
		super.invalidate();
		if (Sides.logical(this).isServer()) {
			network.leave(this);
		}
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
