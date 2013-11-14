package de.take_weiland.mods.cameracraft.networking;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;

import de.take_weiland.mods.cameracraft.api.cable.DataNetwork;
import de.take_weiland.mods.cameracraft.api.cable.NetworkNode;

public class NetworkImpl implements DataNetwork {

	private final List<NetworkNode> nodes;
	
	public NetworkImpl() {
		nodes = Lists.newArrayList();
	}

	@Override
	public Collection<NetworkNode> getNodes() {
		return nodes;
	}

	@Override
	public void join(NetworkNode node) {
		nodes.add(node);
	}

	@Override
	public void leave(NetworkNode node) {
		nodes.remove(node);
	}

	@Override
	public void joinWith(DataNetwork other) {
		Collection<NetworkNode> otherNodes = other.getNodes();
		for (NetworkNode otherNode : otherNodes) {
			otherNode.setNetwork(this);
		}
		nodes.addAll(otherNodes);
	}

	@Override
	public void invalidate() {
		for (NetworkNode node : nodes) {
			node.setNetwork(null);
		}
	}
	
}
