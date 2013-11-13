package de.take_weiland.mods.cameracraft.api.cable;

import java.util.Collection;

public interface DataNetwork {

	Collection<NetworkNode> getNodes();
	
	void join(NetworkNode node);
	
	void leave(NetworkNode node);
	
	void joinWith(DataNetwork other);
	
}
