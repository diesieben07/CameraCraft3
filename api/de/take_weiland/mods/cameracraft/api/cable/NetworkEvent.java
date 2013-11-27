package de.take_weiland.mods.cameracraft.api.cable;

public final class NetworkEvent {

	public final DataNetwork network;
	public final NetworkEvent.Type type;
	public final NetworkNode cause;
	public final Object customData;

	public NetworkEvent(DataNetwork network, NetworkEvent.Type type, NetworkNode cause, Object customData) {
		this.network = network;
		this.type = type;
		this.cause = cause;
		this.customData = customData;
	}

	public static enum Type {
		
		/**
		 * New node added to the network
		 * cause: new node being added
		 */
		NEW_NODE,
		/**
		 * node removed from the network
		 * cause: node being removed
		 */
		NODE_REMOVED,
		/**
		 * network is being rebuild
		 * node: null
		 * customData: the network joining this one 
		 */
		JOIN,
		/**
		 * something else happened, can be node-specific
		 */
		CUSTOM
		
	}
	
}
