package de.take_weiland.mods.cameracraft.tileentity;

import de.take_weiland.mods.cameracraft.api.cable.DataNetwork;
import de.take_weiland.mods.cameracraft.api.cable.NetworkNode;
import de.take_weiland.mods.cameracraft.networking.NetworkUtil;
import de.take_weiland.mods.commons.templates.AbstractTileEntity;

public class TileEntityDataCable extends AbstractTileEntity implements NetworkNode {

	private DataNetwork network;
	
	@Override
	public DataNetwork getNetwork() {
		return network;
	}

	@Override
	public boolean hasNetwork() {
		return network != null;
	}

	@Override
	public void setNetwork(DataNetwork network) {
		this.network = network;
	}

	@Override
	public void validate() {
		super.validate();
		NetworkUtil.initializeNetworking(this);
	}
	
	@Override
	public void invalidate() {
		super.invalidate();
		NetworkUtil.shutdownNetworking(this);
	}

}
