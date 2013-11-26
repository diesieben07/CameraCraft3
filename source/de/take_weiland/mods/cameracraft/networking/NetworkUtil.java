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
	
	private static <T extends TileEntity & NetworkTile> DataNetwork findNetworkFor(NetworkNode node) {
		TileEntity tile = node.getTile();
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
					first.joinWith(nearbyNetwork);
				}
			}
			network = first;
			break;
		}
		System.out.println(network);
		network.join(node);
		return network;
	}
	
	public static <T extends TileEntity & NetworkTile> void initializeNetworking(final T tile) {
		if (Sides.logical(tile).isServer()) {
			NetworkNode node = tile.getNode();
			node.setNetwork(findNetworkFor(node));
//			int chunkX = tile.xCoord >> 4;
//			int chunkZ = tile.zCoord >> 4;
//			if (!tile.worldObj.getChunkProvider().chunkExists(chunkX, chunkZ)) { // don't want to initialize if we're still loading the chunks
//				ChunkloadingHandler.register(tile.worldObj, chunkX, chunkZ, new ChunkloadListener() {
//					
//					@Override
//					public void onChunkLoad() {
//						tile.setNetwork(findNetworkFor(tile));
//					}
//					
//				});
//			} else {
//				tile.setNetwork(findNetworkFor(tile));
//			}
		}
	}
	
	public static <T extends TileEntity & NetworkTile> void shutdownNetworking(T tile) {
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
			
			NetworkNode node = tile.getNode();
			if (adjacentNodes >= 2) { // rebuild the network TODO: optimize this?
				DataNetwork network = node.getNetwork();
				network.leave(node);
				network.invalidate();
				long start = System.nanoTime();
				for (NetworkNode adjNode : network.getNodes()) {
					adjNode.setNetwork(findNetworkFor(adjNode));
				}
				System.out.println("spent " + (System.nanoTime() - start) + " nanos rebuilding");
			} else {
				node.getNetwork().leave(node); // easy
			}
		}
	}

}
