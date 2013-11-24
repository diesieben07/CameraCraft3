package de.take_weiland.mods.cameracraft.api.cable;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Predicate;

public interface DataNetwork {

	List<NetworkNode> getNodes();
	
	Collection<NetworkNode> nodesMatching(Predicate<? super NetworkNode> predicate);
	
	void invalidate();
	
	void join(NetworkNode node);
	
	void leave(NetworkNode node);
	
	void joinWith(DataNetwork other);
	
	void onJoinWith(DataNetwork newNetwork);
	
	void addListener(ChangeListener listener);
	
	void removeListener(ChangeListener listener);

	public static interface ChangeListener {
		
		void onNewNode(DataNetwork network, NetworkNode node);
		
		void onNodeRemoved(DataNetwork network, NetworkNode node);
		
		void onMergeWith(DataNetwork oldNetwork, DataNetwork newNetwork);
		
	}
	
}
