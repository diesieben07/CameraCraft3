package de.take_weiland.mods.cameracraft.networking;

import java.util.Set;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

import de.take_weiland.mods.cameracraft.api.cable.DataNetwork;
import de.take_weiland.mods.cameracraft.api.cable.NetworkNode;
import de.take_weiland.mods.cameracraft.api.cable.NetworkTile;
import de.take_weiland.mods.commons.util.Sides;

public final class NetworkUtil {

	private NetworkUtil() { }
	
	private static <T extends TileEntity & NetworkTile> void findNetworkFor(NetworkNode node, DataNetwork oldNetwork) {
		TileEntity tile = node.getTile();
		if (Sides.logical(tile).isClient()) {
			return;
		}
		Set<DataNetwork> nearbyNetworks = Sets.newHashSetWithExpectedSize(3);
		for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			int x = tile.xCoord + dir.offsetX;
			int z = tile.zCoord + dir.offsetZ;
			
			if (tile.worldObj.getChunkProvider().chunkExists(x >> 4, z >> 4)) {
				TileEntity te = tile.worldObj.getBlockTileEntity(x, tile.yCoord + dir.offsetY, z);
				if (te instanceof NetworkTile && !te.isInvalid()) {
					NetworkNode nearbyNode = ((NetworkTile) te).getNode();
					if (nearbyNode.hasNetwork()) {
						nearbyNetworks.add(nearbyNode.getNetwork());
					}
				}
			}
		}
		DataNetwork network;
		switch (nearbyNetworks.size()) {
		case 0: // we are alone, yay!
			network = new NetworkImpl();
			break;
		case 1: // we have one network nearby, join it
			network = Iterables.getOnlyElement(nearbyNetworks);
			break;
		default: // merge two or more networks
			DataNetwork first = null;
			for (DataNetwork nearbyNetwork : nearbyNetworks) {
				if (first == null) { // yay, ugly hack because Sets don't guarantee iteration order...
					first = nearbyNetwork;
				} else {
					System.out.println(nearbyNetwork.getListeners());
					first.joinWith(nearbyNetwork);
				}
			}
			network = first;
			break;
		}
		if (oldNetwork != null && oldNetwork.getListeners() != null) {
			network.addListeners(oldNetwork.getListeners());
		}
		node.setNetwork(network);
		network.join(node);
//		return network;
	}
	
	public static void initializeNetworking(NetworkNode node) {
		findNetworkFor(node, null);
	}
	
	public static <T extends TileEntity & NetworkTile> void initializeNetworking(final T tile) {
		if (Sides.logical(tile).isServer()) {
			findNetworkFor(tile.getNode(), null);
		}
	}
	
	public static <T extends TileEntity & NetworkTile> void shutdownNetworking(NetworkNode node) {
		TileEntity tile = node.getTile();
		if (Sides.logical(tile).isServer()) {
			int adjacentNodes = 0;
			for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
				int x = tile.xCoord + side.offsetX;
				int z = tile.zCoord + side.offsetZ;
				if (tile.worldObj.getChunkProvider().chunkExists(x >> 4, z >> 4)) {
					TileEntity te = tile.worldObj.getBlockTileEntity(x, tile.yCoord + side.offsetY, z);
					if (te instanceof NetworkTile) {
						adjacentNodes++;
						if (adjacentNodes >= 2) {
							break;
						}
					}
				}
			}
			
			if (adjacentNodes >= 2) { // rebuild the network TODO: optimize this?
				DataNetwork oldNetwork = node.getNetwork();
				oldNetwork.leave(node);
				oldNetwork.invalidate();
				long start = System.nanoTime();
				for (NetworkNode adjNode : oldNetwork.getNodes()) {
					findNetworkFor(adjNode, oldNetwork);
				}
				System.out.println("spent " + (System.nanoTime() - start) + " nanos rebuilding");
			} else {
				node.getNetwork().leave(node); // easy
			}
		}
	}

}
