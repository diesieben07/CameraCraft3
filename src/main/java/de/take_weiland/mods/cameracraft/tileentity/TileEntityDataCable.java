package de.take_weiland.mods.cameracraft.tileentity;

import de.take_weiland.mods.cameracraft.api.cable.NetworkNode;
import de.take_weiland.mods.cameracraft.api.cable.NetworkTile;
import de.take_weiland.mods.cameracraft.networking.NetworkNodeImpl;
import de.take_weiland.mods.commons.templates.AbstractTileEntity;

public class TileEntityDataCable extends AbstractTileEntity implements NetworkTile {

	private NetworkNodeImpl node = new NetworkNodeImpl(this);
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		node.update();
	}

	@Override
	public void invalidate() {
		super.invalidate();
		node.shutdown();
	}

	@Override
	public String getNetworkName() {
		return null;
	}

	@Override
	public NetworkNode getNode() {
		return node;
	}

}
