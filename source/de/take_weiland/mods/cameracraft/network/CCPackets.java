package de.take_weiland.mods.cameracraft.network;

import java.io.InputStream;
import java.util.Map;

import net.minecraft.network.INetworkManager;
import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.commons.network.ModPacket;
import de.take_weiland.mods.commons.network.MultipartPacket.MultipartPacketType;
import de.take_weiland.mods.commons.network.PacketTransport;
import de.take_weiland.mods.commons.network.PacketType;
import de.take_weiland.mods.commons.network.Packets;

public enum CCPackets implements PacketType {
	
	TAKE_PHOTO(PacketTakePhoto.class);

	private final Class<? extends ModPacket> clazz;
	
	private CCPackets(Class<? extends ModPacket> clazz) {
		this.clazz = clazz;
	}
	
	@Override
	public int packetId() {
		return ordinal();
	}

	@Override
	public Class<? extends ModPacket> packetClass() {
		return clazz;
	}

	@Override
	public PacketTransport transport() {
		return CameraCraft.packetTransport;
	}
	
	public static enum MultiPackets implements MultipartPacketType {
		
		PHOTODATA(PacketPhotoData.class);
		
		private static final int CCPACKETS_LENGTH = CCPackets.values().length;

		private final Class<? extends ModPacket> clazz;
		private final Map<INetworkManager, InputStream[]> tracker;
		
		private MultiPackets(Class<? extends ModPacket> clazz) {
			this.clazz = clazz;
			tracker = Packets.newMulitpartTracker();
		}

		@Override
		public int packetId() {
			return CCPACKETS_LENGTH + ordinal();
		}

		@Override
		public PacketTransport transport() {
			return CameraCraft.packetTransport;
		}

		@Override
		public Class<? extends ModPacket> packetClass() {
			return clazz;
		}

		@Override
		public Map<INetworkManager, InputStream[]> tracker() {
			return tracker;
		}
	}
	
}