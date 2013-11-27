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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.primitives.UnsignedBytes;

import de.take_weiland.mods.cameracraft.api.PhotoStorageProvider;
import de.take_weiland.mods.cameracraft.api.cable.NetworkEvent;
import de.take_weiland.mods.cameracraft.api.cable.NetworkListener;
import de.take_weiland.mods.cameracraft.api.cable.NetworkNode;
import de.take_weiland.mods.cameracraft.api.photo.PhotoStorage;
import de.take_weiland.mods.cameracraft.item.CCItem;
import de.take_weiland.mods.cameracraft.tileentity.TilePrinter;
import de.take_weiland.mods.commons.templates.AbstractContainer;
import de.take_weiland.mods.commons.templates.AdvancedSlot;
import de.take_weiland.mods.commons.util.Containers;
import de.take_weiland.mods.commons.util.ItemStacks;
import de.take_weiland.mods.commons.util.Multitypes;
import de.take_weiland.mods.commons.util.Sides;

public class ContainerPrinter extends AbstractContainer.Synced<TilePrinter> implements NetworkListener {

	private boolean clientNeedsRefresh = true;
	
	private ClientNodeInfo[] nodes;
	
	protected ContainerPrinter(World world, int x, int y, int z, EntityPlayer player) {
		super(world, x, y, z, player, Containers.PLAYER_INV_X_DEFAULT, 118);
		if (Sides.logical(world).isServer()) {
			inventory.getNode().getNetwork().addListener(this);
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
	
	public final ClientNodeInfo[] getNodes() {
		return nodes;
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
		int count = in.readUnsignedShort();
		nodes = new ClientNodeInfo[count];
		for (int i = 0; i < count; ++i) {
			String name = in.readUTF();
			int numPhotos = in.readUnsignedByte();
			String[] photoIds = new String[numPhotos];
			for (int j = 0; j < numPhotos; ++j) {
				photoIds[j] = in.readUTF();
			}
			nodes[i] = new ClientNodeInfo(name, photoIds);
		}
		Arrays.sort(nodes);
	}

	@Override
	public boolean writeSyncData(DataOutputStream out) throws IOException {
		if (clientNeedsRefresh) {
			Collection<NetworkNode> toSend = Collections2.filter(inventory.getNode().getNetwork().getNodes(), nodeFilter);
			out.writeShort(toSend.size());
			for (NetworkNode node : toSend) {
				out.writeUTF(node.getDisplayName());
				PhotoStorage storage = ((PhotoStorageProvider) node.getTile()).getPhotoStorage();
				if (storage != null) {
					List<String> photoIds = storage.getPhotos();
					out.writeByte(UnsignedBytes.checkedCast(photoIds.size()));
					for (String photoId : photoIds) {
						out.writeUTF(photoId);
					}
				} else {
					out.writeByte(0);
				}
			}
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
		
		ClientNodeInfo(String displayName, String[] photoIds) {
			this.displayName = displayName;
			this.photoIds = photoIds;
		}

		@Override
		public int compareTo(ClientNodeInfo o) {
			return String.CASE_INSENSITIVE_ORDER.compare(displayName, o.displayName);
		}
		
	}

}
