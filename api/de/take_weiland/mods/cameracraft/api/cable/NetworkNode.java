package de.take_weiland.mods.cameracraft.api.cable;


public interface NetworkNode {

	DataNetwork getNetwork();

	boolean hasNetwork();

	void setNetwork(DataNetwork network);
	
	String getDisplayName();
	
}
