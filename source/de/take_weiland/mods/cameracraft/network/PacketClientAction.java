package de.take_weiland.mods.cameracraft.network;

import static de.take_weiland.mods.commons.network.Packets.readEnum;
import static de.take_weiland.mods.commons.network.Packets.writeEnum;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.relauncher.Side;
import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.commons.network.AbstractPacket;
import de.take_weiland.mods.commons.network.PacketType;

public class PacketClientAction extends AbstractPacket {

	private Action action;
	
	public PacketClientAction(Action action) {
		this.action = action;
	}
	
	@Override
	public void execute(EntityPlayer player, Side side) {
		switch (action) {
		case TAKE_PHOTO:
//			CameraCraft.env.executePhoto();
			break;
		case NAME_PHOTO:
			
			break;
		}
	}

	@Override
	public void read(EntityPlayer player, Side side, InputStream in) throws IOException {
		action = readEnum(in, Action.class);
	}

	@Override
	public void write(OutputStream out) throws IOException {
		writeEnum(out, action);
	}
	
	@Override
	public boolean isValidForSide(Side side) {
		return side.isClient();
	}
	
	@Override
	public PacketType type() {
		return CCPackets.CLIENT_ACTION;
	}
	
	public static enum Action {
		
		TAKE_PHOTO, NAME_PHOTO
		
	}

}
