package de.take_weiland.mods.cameracraft.gui;

import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import de.take_weiland.mods.cameracraft.api.PhotoStorageProvider;
import de.take_weiland.mods.cameracraft.api.cable.NetworkEvent;
import de.take_weiland.mods.cameracraft.api.cable.NetworkListener;
import de.take_weiland.mods.cameracraft.api.cable.NetworkNode;
import de.take_weiland.mods.cameracraft.api.photo.PhotoStorage;
import de.take_weiland.mods.cameracraft.api.printer.InkItem;
import de.take_weiland.mods.cameracraft.network.PacketPrinterGui;
import de.take_weiland.mods.cameracraft.photo.PhotoManager;
import de.take_weiland.mods.cameracraft.tileentity.TilePrinter;
import de.take_weiland.mods.commons.net.DataBuf;
import de.take_weiland.mods.commons.net.WritableDataBuf;
import de.take_weiland.mods.commons.templates.AbstractContainer;
import de.take_weiland.mods.commons.templates.AdvancedSlot;
import de.take_weiland.mods.commons.util.Containers;
import de.take_weiland.mods.commons.util.ItemStacks;
import de.take_weiland.mods.commons.util.JavaUtils;
import de.take_weiland.mods.commons.util.Sides;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ContainerPrinter extends AbstractContainer<TilePrinter> implements NetworkListener {

	private final boolean isAdvanced;

	private boolean clientNeedsRefresh = true;
	
	private List<ClientNodeInfo> nodes;
	private int selectedNode = -1;
	
	protected ContainerPrinter(World world, int x, int y, int z, EntityPlayer player, boolean isAdvanced) {
		super(world, x, y, z, player, Containers.PLAYER_INV_X_DEFAULT, 118);
		this.isAdvanced = isAdvanced;

		if (Sides.logical(world).isServer()) {
			inventory.getNode().getNetwork().addListener(this);
		} else {
			nodes = Lists.newArrayList();
		}
	}

	public boolean isAdvanced() {
		return isAdvanced;
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		if (Sides.logical(player).isServer()) {
			inventory.getNode().getNetwork().removeListener(this);
		}
	}

	@Override
	protected int getSlotFor(ItemStack stack) {
		Item item;
		if (ItemStacks.is(stack, Item.paper)) {
			return 4;
		} else if ((item = stack.getItem()) instanceof InkItem) {
			return TilePrinter.INK_COLOR_TO_SLOT[((InkItem)item).getColor(stack).ordinal()];
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
		return JavaUtils.get(nodes, selectedNode);
	}
	
	private final Predicate<NetworkNode> nodeFilter = new Predicate<NetworkNode>() {

		@Override
		public boolean apply(NetworkNode node) {
			TileEntity tile = node.getTile();
			return tile != inventory() && tile instanceof PhotoStorageProvider && node.getDisplayName() != null;
		}
		
	};

	public void readData(DataBuf in) {
		selectedNode = -1;
		nodes.clear();
		String name;
		do {
			name = in.getString();
			if (name != null) {
				int photoCount = in.getVarInt();
				String[] photoIds = new String[photoCount];
				for (int i = 0; i < photoCount; ++i) {
					photoIds[i] = PhotoManager.asString(in.getVarInt());
				}
				nodes.add(new ClientNodeInfo(name, photoIds));
			}
		} while (name != null);
		Collections.sort(nodes);
	}

	public void writeData(WritableDataBuf out) {
		if (clientNeedsRefresh) {
			Collection<NetworkNode> toSend = Collections2.filter(inventory.getNode().getNetwork().getNodes(), nodeFilter);
			
			for (NetworkNode node : toSend) {
				String name = node.getDisplayName();
				out.putString(Strings.nullToEmpty(node.getDisplayName()));
				PhotoStorage storage = ((PhotoStorageProvider) node.getTile()).getPhotoStorage();
				if (storage != null) {
					int[] photoIds = storage.getRawPhotoIds();
					out.putVarInt(photoIds.length);
					for (int photoId : photoIds) {
						out.putVarInt(photoId);
					}
				} else {
					out.putVarInt(0);
				}
			}
			out.putString(null);
			clientNeedsRefresh = false;
		}
	}

	@Override
	public void detectAndSendChanges() {
		if (clientNeedsRefresh) {
			new PacketPrinterGui(this).sendToViewing(this);
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
