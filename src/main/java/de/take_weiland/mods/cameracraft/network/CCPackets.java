package de.take_weiland.mods.cameracraft.network;

import de.take_weiland.mods.commons.net.ModPacket;
import de.take_weiland.mods.commons.net.SimplePacketType;

/**
* @author diesieben07
*/
public enum CCPackets implements SimplePacketType {

	CLIENT_ACTION(PacketClientAction.class),
	TAKEN_PHOTO(PacketTakenPhoto.class),
	PHOTO_NAME(PacketPhotoName.class),
	REQUEST_PHOTO(PacketRequestStandardPhoto.class),
	CLIENT_REQUEST_PHOTO(PacketClientRequestPhoto.class),
	PHOTO_DATA(PacketPhotoData.class),
	PRINT_JOB(PacketPrintJobs.class),
	PRINTER_GUI(PacketPrinterGui.class);

	private final Class<? extends ModPacket> clazz;

	CCPackets(Class<? extends ModPacket> clazz) {
		this.clazz = clazz;
	}

	@Override
	public Class<? extends ModPacket> packet() {
		return clazz;
	}

}
