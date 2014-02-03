package de.take_weiland.mods.cameracraft.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import de.take_weiland.mods.cameracraft.api.photo.PhotoItem;
import de.take_weiland.mods.commons.net.DataBuf;
import de.take_weiland.mods.commons.net.WritableDataBuf;

public class PacketPhotoName extends CCPacket {

	private String name;
	
	public PacketPhotoName(String name) {
		this.name = name;
	}
	
	@Override
	protected void write(WritableDataBuf buf) {
		buf.putString(name);
	}

	@Override
	protected void handle(DataBuf buf, EntityPlayer player, Side side) {
		String name = buf.getString();
		ItemStack current = player.getCurrentEquippedItem();
		if (current != null) {
			Item item = current.getItem();
			if (item instanceof PhotoItem) {
				((PhotoItem) item).setName(current, name);
			}
		}
	}

	@Override
	protected boolean validOn(Side side) {
		return side.isServer();
	}

}
