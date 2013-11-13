package de.take_weiland.mods.cameracraft.networking;

import java.util.Set;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import de.take_weiland.mods.cameracraft.api.cable.DataNetwork;
import de.take_weiland.mods.cameracraft.api.cable.NetworkNode;
import de.take_weiland.mods.commons.util.Sides;

public final class NetworkUtil {

	private NetworkUtil() { }
	
	public static <T extends TileEntity & NetworkNode> DataNetwork findNetworkFor(T tile) {
		Set<DataNetwork> nearbyNetworks = Sets.newHashSetWithExpectedSize(3);
		for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			TileEntity te = tile.worldObj.getBlockTileEntity(tile.xCoord + dir.offsetX, tile.yCoord + dir.offsetY, tile.zCoord + dir.offsetZ);
			if (te instanceof NetworkNode && ((NetworkNode)te).hasNetwork()) {
				nearbyNetworks.add(((NetworkNode) te).getNetwork());
			}
		}
		DataNetwork network;
		switch (nearbyNetworks.size()) {
		case 0: // we are alone, yay!
			network = new NetworkImpl();
			break;
		case 1: // we have a network nearby, join it
			network = Iterables.getOnlyElement(nearbyNetworks);
			break;
		default: // join two or more networks
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
			int chunkX = tile.xCoord >> 4;
			int chunkZ = tile.zCoord >> 4;
			if (!tile.worldObj.getChunkProvider().chunkExists(chunkX, chunkZ)) { // don't want to initialize if we're still loading the chunks
				ChunkloadingHandler.get(tile.worldObj).registerListener(chunkX, chunkZ, new ChunkloadListener() {
					
					@Override
					public void onChunkLoad() {
						tile.setNetwork(findNetworkFor(tile));
					}
					
				});
			} else {
				tile.setNetwork(findNetworkFor(tile));
			}
		}
	}
	
	public static <T extends TileEntity & NetworkNode> void shutdownNetworking(T tile) {
		if (Sides.logical(tile).isServer()) {
			tile.getNetwork().leave(tile);
		}
	}

}
