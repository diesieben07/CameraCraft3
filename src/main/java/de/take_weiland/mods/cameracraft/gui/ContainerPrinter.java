package de.take_weiland.mods.cameracraft.gui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.primitives.UnsignedBytes;

import de.take_weiland.mods.cameracraft.api.PhotoStorageProvider;
import de.take_weiland.mods.cameracraft.api.cable.NetworkEvent;
import de.take_weiland.mods.cameracraft.api.cable.NetworkListener;
import de.take_weiland.mods.cameracraft.api.cable.NetworkNode;
import de.take_weiland.mods.cameracraft.api.photo.PhotoStorage;
import de.take_weiland.mods.cameracraft.item.CCItem;
import de.take_weiland.mods.cameracraft.photo.PhotoManager;
import de.take_weiland.mods.cameracraft.tileentity.TilePrinter;
import de.take_weiland.mods.commons.templates.AbstractContainer;
import de.take_weiland.mods.commons.templates.AdvancedSlot;
import de.take_weiland.mods.commons.util.Containers;
import de.take_weiland.mods.commons.util.ItemStacks;
import de.take_weiland.mods.commons.util.JavaUtils;
import de.take_weiland.mods.commons.util.Multitypes;
import de.take_weiland.mods.commons.util.Sides;

public class ContainerPrinter extends AbstractContainer.Synced<TilePrinter> implements NetworkListener {

	private boolean clientNeedsRefresh = true;
	
	private List<ClientNodeInfo> nodes;
	private int selectedNode = -1;
	
	protected ContainerPrinter(World world, int x, int y, int z, EntityPlayer player) {
		super(world, x, y, z, player, Containers.PLAYER_INV_X_DEFAULT, 118);
		if (Sides.logical(world).isServer()) {
			inventory.getNode().getNetwork().addListener(this);
		} else {
			nodes = Lists.newArrayList();
		}
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		if (Sides.logical(player).isServer()) {
			inventory.getNode().getNetwork().removeListener(this);
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
	
	public final List<ClientNodeInfo> getNodes() {
		return nodes;
	}
	
	public final ClientNodeInfo getSelectedNode() {
		return JavaUtils.safeListAccess(nodes, selectedNode);
	}
	
	private final Predicate<NetworkNode> nodeFilter = new Predicate<NetworkNode>() {

		@Override
		public boolean apply(NetworkNode node) {
			TileEntity tile = node.getTile();
			return tile != inventory() && tile instanceof PhotoStorageProvider && node.getDisplayName() != null;
		}
		
	};

	@Override
	public void readSyncData(DataInputStream in) throws IOException {
		selectedNode = -1;
		nodes.clear();
		int nameLen;
		do {
			nameLen = in.readShort();
			if (nameLen >= 0) {
				StringBuilder b = new StringBuilder();
				for (int i = 0; i < nameLen; ++i) {
					b.append(in.readChar());
				}
				int photoCount = in.readUnsignedByte();
				String[] photoIds = null;
				photoIds = new String[photoCount];
				for (int j = 0; j < photoCount; ++j) {
					photoIds[j] = PhotoManager.asString(in.readInt());
				}
				nodes.add(new ClientNodeInfo(b.toString(), photoIds));
			}
		} while (nameLen >= 0);
		Collections.sort(nodes);
	}

	@Override
	public boolean writeSyncData(DataOutputStream out) throws IOException {
		if (clientNeedsRefresh) {
			Collection<NetworkNode> toSend = Collections2.filter(inventory.getNode().getNetwork().getNodes(), nodeFilter);
			
			for (NetworkNode node : toSend) {
				String name = node.getDisplayName();
				out.writeShort(name.length());
				out.writeChars(name);
				PhotoStorage storage = ((PhotoStorageProvider) node.getTile()).getPhotoStorage();
				if (storage != null) {
					int[] photoIds = storage.getRawPhotoIds();
					out.writeByte(UnsignedBytes.checkedCast(photoIds.length));
					for (int photoId : photoIds) {
						out.writeInt(photoId);
					}
				} else {
					out.writeByte(0);
				}
			}
			out.writeShort(-1);
			clientNeedsRefresh = false;
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void handleEvent(NetworkEvent event) {
		clientNeedsRefresh = true;
	}
	
	public static final class ClientNodeInfo implements Comparable<ClientNodeInfo> {
		
		public final String displayName;
		public final String[] photoIds;
		public final int[] counts;
		
		ClientNodeInfo(String displayName, String[] photoIds) {
			this.displayName = displayName;
			this.photoIds = photoIds;
			counts = photoIds == null ? null : new int[photoIds.length];
		}

		@Override
		public int compareTo(ClientNodeInfo o) {
			return String.CASE_INSENSITIVE_ORDER.compare(displayName, o.displayName);
		}
		
	}

	public int getSelectedNodeIdx() {
		return selectedNode;
	}

	public void selectNode(int selectedNode) {
		this.selectedNode = selectedNode;
	}

}
