package de.take_weiland.mods.cameracraft.networks;

import com.google.common.primitives.UnsignedBytes;

import de.take_weiland.mods.commons.network.PacketType;
import de.take_weiland.mods.commons.network.StreamPacket;

public enum CCPackets implements PacketType {
	
	;
	
	private static final String CHANNEL = "CameraCraft";

	private final Class<? extends StreamPacket> clazz;
	
	private CCPackets(Class<? extends StreamPacket> clazz) {
		this.clazz = clazz;
	}
	
	@Override
	public String getChannel() {
		return CHANNEL;
	}

	@Override
	public byte getPacketId() {
		return UnsignedBytes.checkedCast(ordinal());
	}

	@Override
	public Class<? extends StreamPacket> getPacketClass() {
		return clazz;
	}

}
