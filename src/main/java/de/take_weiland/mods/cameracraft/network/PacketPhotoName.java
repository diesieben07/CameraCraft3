package de.take_weiland.mods.cameracraft.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import de.take_weiland.mods.cameracraft.api.photo.PhotoItem;
import de.take_weiland.mods.commons.network.DataPacket;
import de.take_weiland.mods.commons.network.PacketType;

public class PacketPhotoName extends DataPacket {

	private String name;
	
	public PacketPhotoName(String name) {
		this.name = name;
	}

	@Override
	protected void write(DataOutputStream out) throws IOException {
		out.writeUTF(name);
	}

	@Override
	protected void read(EntityPlayer player, Side side, DataInputStream in) throws IOException {
		name = in.readUTF();
	}
	
	@Override
	public void execute(EntityPlayer player, Side side) {
		ItemStack current = player.getCurrentEquippedItem();
		if (current != null) {
			Item item = current.getItem();
			if (item instanceof PhotoItem) {
				((PhotoItem) item).setName(current, name);
			}
		}
	}

	@Override
	public boolean isValidForSide(Side side) {
		return side.isServer();
	}

	@Override
	public PacketType type() {
		return CCPackets.PHOTO_NAME;
	}
	
}
