package de.take_weiland.mods.cameracraft.network;

import de.take_weiland.mods.commons.net.ModPacket;
import de.take_weiland.mods.commons.net.SimplePacketType;

public abstract class CCPacket extends ModPacket<CCPacket.Type> {

	public static enum Type implements SimplePacketType<Type> {

		CLIENT_ACTION(PacketClientAction.class),
		TAKEN_PHOTO(PacketTakenPhoto.class),
		PHOTO_NAME(PacketPhotoName.class),
		REQUEST_PHOTO(PacketRequestPhoto.class),
		CLIENT_REQUEST_PHOTO(PacketClientRequestPhoto.class),
		PHOTO_DATA(PacketPhotoData.class),
		PRINT_JOB(PacketPrintJobs.class),
		PRINTER_GUI(PacketPrinterGui.class);

		private final Class<? extends ModPacket<Type>> clazz;

		private Type(Class<? extends ModPacket<Type>> clazz) {
			this.clazz = clazz;
		}

		@Override
		public Class<? extends ModPacket<Type>> packet() {
			return clazz;
		}

	}
}
