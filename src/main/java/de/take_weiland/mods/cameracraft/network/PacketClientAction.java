package de.take_weiland.mods.cameracraft.network;

import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.relauncher.Side;
import de.take_weiland.mods.commons.net.DataBuf;
import de.take_weiland.mods.commons.net.Packets;
import de.take_weiland.mods.commons.net.WritableDataBuf;

public class PacketClientAction extends CCPacket {

	private Action action;
	
	public PacketClientAction(Action action) {
		this.action = action;
	}
	
	@Override
	protected boolean validOn(Side side) {
		return side.isClient();
	}

	@Override
	protected void write(WritableDataBuf buffer) {
		Packets.writeEnum(buffer, action);
	}

	@Override
	protected void handle(DataBuf buffer, EntityPlayer player, Side side) {
		switch (Packets.readEnum(buffer, Action.class)) {
		case TAKE_PHOTO:
			break;
		case NAME_PHOTO:
			break;
		}
	}
	
	public static enum Action {
		
		TAKE_PHOTO, NAME_PHOTO
		
	}

}
