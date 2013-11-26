package de.take_weiland.mods.cameracraft.api.cable;

public interface NetworkNode {

	DataNetwork getNetwork();

	boolean hasNetwork();

	void setNetwork(DataNetwork network);
	
	String getDisplayName();
	
	void onNetworkChange();
	
	void addListener(ChangeListener listener);
	
	void removeListener(ChangeListener listener);
	
	public static interface ChangeListener {
		
		void onNewNetwork(NetworkNode node, DataNetwork oldNetwork);
		
		void onNetworkChange(NetworkNode node);
		
	}
	
}
