package de.take_weiland.mods.cameracraft.networking;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import de.take_weiland.mods.cameracraft.api.cable.DataNetwork;
import de.take_weiland.mods.cameracraft.api.cable.NetworkEvent;
import de.take_weiland.mods.cameracraft.api.cable.NetworkListener;
import de.take_weiland.mods.cameracraft.api.cable.NetworkNode;

public class NetworkImpl implements DataNetwork {

	private boolean isValid = true;
	private final List<NetworkNode> nodes;
	private List<NetworkListener> listeners;
	
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
		dispatch(new NetworkEvent(this, NetworkEvent.Type.NEW_NODE, node, null));
	}

	@Override
	public void leave(NetworkNode node) {
		nodes.remove(node);
		dispatch(new NetworkEvent(this, NetworkEvent.Type.NODE_REMOVED, node, null));
	}

	@Override
	public void joinWith(DataNetwork other) {
		Collection<NetworkNode> otherNodes = other.getNodes();
		for (NetworkNode otherNode : otherNodes) {
			otherNode.setNetwork(this);
		}
		nodes.addAll(otherNodes);
		Collection<NetworkListener> otherListeners = other.getListeners();
		if (otherListeners != null) {
			System.out.println(otherListeners);
			writeAccListeners().addAll(otherListeners);
			System.out.println(listeners);
		}
		NetworkEvent event = new NetworkEvent(this, NetworkEvent.Type.JOIN, null, other);
		dispatch(event);
	}

//	private void fireOnChange() {
//		int l = nodes.size();
//		for (int i = 0; i < l; ++i) {
//			nodes.get(i).onNetworkChange();
//		}
//	}

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

	@Override
	public void dispatch(NetworkEvent event) {
		dispatchTo(nodes, event);
		dispatchTo(listeners, event);
	}
	
	private void dispatchTo(List<? extends NetworkListener> listeners, NetworkEvent event) {
		if (listeners != null) {
			int len = listeners.size();
			for (int i = 0; i < len; ++i) {
				listeners.get(i).handleEvent(event);
			}
		}
	}

	@Override
	public void addListener(NetworkListener listener) {
		writeAccListeners().add(listener);
	}

	private List<NetworkListener> writeAccListeners() {
		return writeAccListeners(3);
	}
	
	private List<NetworkListener> writeAccListeners(int cap) {
		return listeners == null ? (listeners = Lists.newArrayListWithCapacity(cap)) : listeners;
	}

	@Override
	public void removeListener(NetworkListener listener) {
		if (listeners != null) {
			listeners.remove(listener);
		}
	}

	@Override
	public Collection<NetworkListener> getListeners() {
		return listeners;
	}

	@Override
	public void addListeners(Collection<NetworkListener> listeners) {
		writeAccListeners(listeners.size() + 3).addAll(listeners);
	}

}
