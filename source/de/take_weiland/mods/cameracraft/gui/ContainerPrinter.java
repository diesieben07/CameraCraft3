package de.take_weiland.mods.cameracraft.gui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import de.take_weiland.mods.cameracraft.api.PhotoStorageProvider;
import de.take_weiland.mods.cameracraft.api.cable.DataNetwork;
import de.take_weiland.mods.cameracraft.api.cable.NetworkNode;
import de.take_weiland.mods.cameracraft.item.CCItem;
import de.take_weiland.mods.cameracraft.tileentity.TilePrinter;
import de.take_weiland.mods.commons.templates.AbstractContainer;
import de.take_weiland.mods.commons.templates.AdvancedSlot;
import de.take_weiland.mods.commons.util.Containers;
import de.take_weiland.mods.commons.util.ItemStacks;
import de.take_weiland.mods.commons.util.Multitypes;
import de.take_weiland.mods.commons.util.Sides;

public class ContainerPrinter extends AbstractContainer.Synced<TilePrinter> implements NetworkNode.ChangeListener {

	private List<String> nodeNames;
	private boolean clientNeedsRefresh = true;
	
	protected ContainerPrinter(World world, int x, int y, int z, EntityPlayer player) {
		super(world, x, y, z, player, Containers.PLAYER_INV_X_DEFAULT, 118);
		if (Sides.logical(world).isServer()) {
			inventory.getNode().addListener(this);
		}
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		if (Sides.logical(player).isServer()) {
			inventory.getNode().removeListener(this);
		}
	}

	@Override
	protected int getSlotFor(ItemStack item) {
		if (ItemStacks.is(item, CCItem.miscItems)) {
			return Multitypes.getType(CCItem.miscItems, item).ordinal() - 1;
		} else if (ItemStacks.is(item, Item.paper)) {
			return 4;
		} else {
			return -1;
		}
	}

	@Override
	protected void addSlots() {
		for (int x = 0; x < 5; ++x) {
			addSlotToContainer(new AdvancedSlot(inventory, x, x * 27 + 44, 91));
		}
	}
	
	public final List<String> getNodeNames() {
		return nodeNames;
	}
	
	private final Predicate<NetworkNode> nodeFilter = new Predicate<NetworkNode>() {

		@Override
		public boolean apply(NetworkNode node) {
			return node.getTile() != inventory() && node.getDisplayName() != null && node.getTile() instanceof PhotoStorageProvider;
		}
		
	};

	@Override
	public void readSyncData(DataInputStream in) throws IOException {
		int count = in.readUnsignedShort();
		String[] nodes = new String[count];
		for (int i = 0; i < count; ++i) {
			nodes[i] = in.readUTF();
		}
		Arrays.sort(nodes, String.CASE_INSENSITIVE_ORDER);
		nodeNames = Arrays.asList(nodes);
	}

	@Override
	public boolean writeSyncData(DataOutputStream out) throws IOException {
		if (clientNeedsRefresh) {
			Collection<NetworkNode> toSend = Collections2.filter(inventory.getNode().getNetwork().getNodes(), nodeFilter);
			out.writeShort(toSend.size());
			for (NetworkNode node : toSend) {
				out.writeUTF(node.getDisplayName());
			}
			clientNeedsRefresh = false;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void onNewNetwork(NetworkNode node, DataNetwork oldNetwork) {
		clientNeedsRefresh = true;
	}

	@Override
	public void onNetworkChange(NetworkNode node) {
		clientNeedsRefresh = true;
	}

}
