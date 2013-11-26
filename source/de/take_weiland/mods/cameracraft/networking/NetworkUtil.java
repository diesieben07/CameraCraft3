package de.take_weiland.mods.cameracraft.networking;

import java.util.Set;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

import de.take_weiland.mods.cameracraft.api.cable.DataNetwork;
import de.take_weiland.mods.cameracraft.api.cable.NetworkNode;
import de.take_weiland.mods.commons.util.Sides;

public final class NetworkUtil {

	private NetworkUtil() { }
	
	static <T extends TileEntity & NetworkNode> DataNetwork findNetworkFor(T tile) {
		Set<DataNetwork> nearbyNetworks = Sets.newHashSetWithExpectedSize(3);
		for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			int x = tile.xCoord + dir.offsetX;
			int z = tile.zCoord + dir.offsetZ;
			
			if (tile.worldObj.getChunkProvider().chunkExists(x >> 4, z >> 4)) {
				TileEntity te = tile.worldObj.getBlockTileEntity(x, tile.yCoord + dir.offsetY, z);
				if (te instanceof NetworkNode && ((NetworkNode)te).hasNetwork() && !te.isInvalid()) {
					nearbyNetworks.add(((NetworkNode) te).getNetwork());
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
		
		network.join(tile);
		return network;
	}
	
	public static <T extends TileEntity & NetworkNode> void initializeNetworking(final T tile) {
		if (Sides.logical(tile).isServer()) {
			tile.setNetwork(findNetworkFor(tile));
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
	
	@SuppressWarnings("unchecked")
	public static <T extends TileEntity & NetworkNode> void shutdownNetworking(T tile) {
		if (Sides.logical(tile).isServer()) {
			int adjacentNodes = 0;
			for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
				int x = tile.xCoord + side.offsetX;
				int z = tile.zCoord + side.offsetZ;
				if (tile.worldObj.getChunkProvider().chunkExists(x >> 4, z >> 4)) {
					TileEntity te = tile.worldObj.getBlockTileEntity(x, tile.yCoord + side.offsetY, z);
					if (te instanceof NetworkNode) {
						adjacentNodes++;
						if (adjacentNodes >= 2) {
							break;
						}
					}
				}
			}
			
			if (adjacentNodes >= 2) { // rebuild the network TODO: optimize this?
				DataNetwork network = tile.getNetwork();
				network.leave(tile);
				network.invalidate();
				long start = System.nanoTime();
				for (NetworkNode node : network.getNodes()) {
					node.setNetwork(findNetworkFor((T) node));
				}
				System.out.println("spent " + (System.nanoTime() - start) + " nanos rebuilding");
			} else {
				tile.getNetwork().leave(tile); // easy
			}
		}
	}

}
