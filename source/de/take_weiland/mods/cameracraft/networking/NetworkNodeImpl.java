package de.take_weiland.mods.cameracraft.networking;

import java.util.List;

import net.minecraft.tileentity.TileEntity;

import com.google.common.collect.Lists;

import de.take_weiland.mods.cameracraft.api.cable.DataNetwork;
import de.take_weiland.mods.cameracraft.api.cable.NetworkNode;
import de.take_weiland.mods.cameracraft.api.cable.NetworkTile;

public class NetworkNodeImpl implements NetworkNode {

	private DataNetwork network;
	private List<NetworkNode.ChangeListener> listeners;
	private int id;
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
		DataNetwork oldNetwork = this.network;
		this.network = network;
		if (listeners != null) {
			int l = listeners.size();
			for (int i = 0; i < l; ++i) {
				listeners.get(i).onNewNetwork(this, oldNetwork);
			}
		}
	}

	@Override
	public void onNetworkChange() {
		if (listeners != null) {
			int l = listeners.size();
			for (int i = 0; i < l; ++i) {
				listeners.get(i).onNetworkChange(this);
			}
		}
	}

	@Override
	public void assignId(int id) {
		this.id = id;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void addListener(ChangeListener listener) {
		(listeners == null ? (listeners = Lists.newArrayListWithCapacity(3)) : listeners).add(listener);
	}

	@Override
	public void removeListener(ChangeListener listener) {
		if (listeners != null) {
			listeners.remove(listener);
		}
	}

	@Override
	public TileEntity getTile() {
		return tile;
	}

	@Override
	public String getDisplayName() {
		return ((NetworkTile) tile).getDisplayName();
	}

}
