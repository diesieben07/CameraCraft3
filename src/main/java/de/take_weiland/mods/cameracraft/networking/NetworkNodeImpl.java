package de.take_weiland.mods.cameracraft.networking;

import net.minecraft.tileentity.TileEntity;
import de.take_weiland.mods.cameracraft.api.cable.DataNetwork;
import de.take_weiland.mods.cameracraft.api.cable.NetworkEvent;
import de.take_weiland.mods.cameracraft.api.cable.NetworkNode;
import de.take_weiland.mods.cameracraft.api.cable.NetworkTile;

public class NetworkNodeImpl implements NetworkNode {

	private DataNetwork network;
//	private List<NetworkNode.ChangeListener> listeners;
	private final TileEntity tile;
	
	public <T extends TileEntity & NetworkTile> NetworkNodeImpl(T tile) {
		this.tile = tile;
	}

	@Override
	public DataNetwork getNetwork() {
		return network;
	}

	@Override
	public boolean hasNetwork() {
		return network != null && network.isValid();
	}

	@Override
	public void setNetwork(DataNetwork network) {
		this.network = network;
	}

	@Override
	public TileEntity getTile() {
		return tile;
	}

	@Override
	public String getDisplayName() {
		return ((NetworkTile) tile).getDisplayName();
	}
	
	@Override
	public void handleEvent(NetworkEvent event) { }
	
	public void update() {
		if (!hasNetwork()) {
			NetworkUtil.initializeNetworking(this);
		}
	}
	
	public void shutdown() {
		NetworkUtil.shutdownNetworking(this);
	}

}
