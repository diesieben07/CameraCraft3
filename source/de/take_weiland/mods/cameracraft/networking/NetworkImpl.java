package de.take_weiland.mods.cameracraft.networking;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import de.take_weiland.mods.cameracraft.api.cable.DataNetwork;
import de.take_weiland.mods.cameracraft.api.cable.NetworkNode;

public class NetworkImpl implements DataNetwork {

	private final List<NetworkNode> nodes;
	
	public NetworkImpl() {
		nodes = Lists.newArrayList();
	}

	@Override
	public List<NetworkNode> getNodes() {
		return nodes;
	}

	@Override
	public void join(NetworkNode node) {
		nodes.add(node);
		if (listeners == null) {
			return;
		}
		List<ChangeListener> listeners = this.listeners;
		int l = listeners.size();
		for (int i = 0; i < l; ++i) {
			listeners.get(i).onNewNode(this, node);
		}
	}

	@Override
	public void leave(NetworkNode node) {
		nodes.remove(node);
		if (listeners == null) {
			return;
		}
		List<ChangeListener> listeners = this.listeners;
		int l = listeners.size();
		for (int i = 0; i < l; ++i) {
			listeners.get(i).onNodeRemoved(this, node);
		}
	}

	@Override
	public void joinWith(DataNetwork other) {
		Collection<NetworkNode> otherNodes = other.getNodes();
		for (NetworkNode otherNode : otherNodes) {
			otherNode.setNetwork(this);
		}
		nodes.addAll(otherNodes);
		other.onJoinWith(this);
	}
	
	@Override
	public void onJoinWith(DataNetwork newNetwork) {
		if (listeners == null) {
			return;
		}
		List<ChangeListener> listeners = this.listeners;
		int l = listeners.size();
		for (int i = 0; i < l; ++i) {
			listeners.get(i).onMergeWith(this, newNetwork);
		}
	}

	@Override
	public void invalidate() {
		for (NetworkNode node : nodes) {
			node.setNetwork(null);
		}
	}

	@Override
	public Collection<NetworkNode> nodesMatching(Predicate<? super NetworkNode> predicate) {
		return Collections2.filter(nodes, predicate);
	}
	
	private List<ChangeListener> listeners;

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

}
