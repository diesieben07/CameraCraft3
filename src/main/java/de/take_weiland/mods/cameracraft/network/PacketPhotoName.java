package de.take_weiland.mods.cameracraft.network;

import cpw.mods.fml.relauncher.Side;
import de.take_weiland.mods.cameracraft.api.photo.PhotoItem;
import de.take_weiland.mods.commons.net.MCDataInput;
import de.take_weiland.mods.commons.net.MCDataOutput;
import de.take_weiland.mods.commons.net.Packet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@Packet.Receiver(Side.SERVER)
public class PacketPhotoName implements Packet {

	private String name;
	
	public PacketPhotoName(String name) {
		this.name = name;
	}

	public PacketPhotoName(MCDataInput in) {
		this.name = in.readString();
	}

	@Override
	public void writeTo(MCDataOutput out) {
		out.writeString(name);
	}

	public void handle(EntityPlayer player) {
		ItemStack current = player.getCurrentEquippedItem();
		if (current != null) {
			Item item = current.getItem();
			if (item instanceof PhotoItem) {
				((PhotoItem) item).setName(current, name);
			}
		}
	}
}
