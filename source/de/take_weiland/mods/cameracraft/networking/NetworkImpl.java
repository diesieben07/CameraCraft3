package de.take_weiland.mods.cameracraft.networking;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import de.take_weiland.mods.cameracraft.api.cable.DataNetwork;
import de.take_weiland.mods.cameracraft.api.cable.NetworkNode;

public class NetworkImpl implements DataNetwork {

	private int nextId = 0;
	private boolean isValid = true;
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
		node.assignId(nextId++);
		fireOnChange();
	}

	@Override
	public void leave(NetworkNode node) {
		nodes.remove(node);
		fireOnChange();
	}

	@Override
	public void joinWith(DataNetwork other) {
		Collection<NetworkNode> otherNodes = other.getNodes();
		for (NetworkNode otherNode : otherNodes) {
			otherNode.setNetwork(this);
		}
		nodes.addAll(otherNodes);
	}

	private void fireOnChange() {
		int l = nodes.size();
		for (int i = 0; i < l; ++i) {
			nodes.get(i).onNetworkChange();
		}
	}

	@Override
	public void invalidate() {
		isValid = false;
	}
	
	@Override
	public boolean isValid() {
		return isValid;
	}

	@Override
	public Collection<NetworkNode> nodesMatching(Predicate<? super NetworkNode> predicate) {
		return Collections2.filter(nodes, predicate);
	}

}
