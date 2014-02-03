package de.take_weiland.mods.cameracraft.network;

import de.take_weiland.mods.commons.net.ModPacket;
import de.take_weiland.mods.commons.net.SimplePacketType;

public enum CCPackets implements SimplePacketType<CCPackets> {
	
	CLIENT_ACTION(PacketClientAction.class),
	TAKEN_PHOTO(PacketTakenPhoto.class),
	PHOTO_NAME(PacketPhotoName.class),
	REQUEST_PHOTO(PacketRequestPhoto.class),
	CLIENT_REQUEST_PHOTO(PacketClientRequestPhoto.class),
	PHOTO_DATA(PacketPhotoData.class),
	PRINT_JOB(PacketPrintJobs.class);

	private final Class<? extends ModPacket<CCPackets>> clazz;
	
	private CCPackets(Class<? extends ModPacket<CCPackets>> clazz) {
		this.clazz = clazz;
	}
	
	@Override
	public Class<? extends ModPacket<CCPackets>> packet() {
		return clazz;
	}

}