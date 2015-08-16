package de.take_weiland.mods.cameracraft.network;

import cpw.mods.fml.relauncher.Side;
import de.take_weiland.mods.commons.net.MCDataInput;
import de.take_weiland.mods.commons.net.MCDataOutput;
import de.take_weiland.mods.commons.net.Packet;
import net.minecraft.entity.player.EntityPlayer;

@Packet.Receiver(Side.CLIENT)
public class PacketClientAction implements Packet {

	private Action action;
	
	public PacketClientAction(Action action) {
		this.action = action;
	}

	public PacketClientAction(MCDataInput in) {
        this.action = in.readEnum(Action.class);
    }

    @Override
    public void writeTo(MCDataOutput out) {
        out.writeEnum(action);
    }

    public void handle(EntityPlayer player, Side side) {
		switch (action) {
		case TAKE_PHOTO:
			break;
		case NAME_PHOTO:
			break;
		}
	}
	
	public enum Action {
		
		TAKE_PHOTO, NAME_PHOTO
		
	}

}
