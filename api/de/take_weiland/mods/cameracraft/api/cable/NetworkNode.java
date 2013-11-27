package de.take_weiland.mods.cameracraft.api.cable;

import net.minecraft.tileentity.TileEntity;

public interface NetworkNode extends NetworkListener {

	TileEntity getTile();
	
	String getDisplayName();
	
	DataNetwork getNetwork();

	boolean hasNetwork();

	void setNetwork(DataNetwork network);
	
//	void onNetworkChange();
//	
//	void addListener(ChangeListener listener);
//	
//	void removeListener(ChangeListener listener);
//	
//	public static interface ChangeListener {
//		
//		void onNewNetwork(NetworkNode node, DataNetwork oldNetwork);
//		
//		void onNetworkChange(NetworkNode node);
//		
//	}
	
}
