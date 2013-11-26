package de.take_weiland.mods.cameracraft.tileentity;

import java.util.List;

import com.google.common.collect.Lists;

import de.take_weiland.mods.cameracraft.api.cable.DataNetwork;
import de.take_weiland.mods.cameracraft.api.cable.NetworkNode;
import de.take_weiland.mods.cameracraft.api.cable.NetworkNode.ChangeListener;
import de.take_weiland.mods.cameracraft.networking.NetworkUtil;
import de.take_weiland.mods.commons.templates.AbstractTileEntity;

public class TileEntityDataCable extends AbstractTileEntity implements NetworkNode {

	private DataNetwork network;
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		if (network == null) {
			NetworkUtil.initializeNetworking(this);
		}
	}

	@Override
	public DataNetwork getNetwork() {
		return network;
	}

	@Override
	public boolean hasNetwork() {
		return network != null && network.isValid();
	}

	private List<NetworkNode.ChangeListener> listeners;
	
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
	public void invalidate() {
		super.invalidate();
		NetworkUtil.shutdownNetworking(this);
	}

	@Override
	public String getDisplayName() {
		return null;
	}

}
