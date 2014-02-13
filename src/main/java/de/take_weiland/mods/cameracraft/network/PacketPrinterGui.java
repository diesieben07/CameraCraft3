package de.take_weiland.mods.cameracraft.network;

import cpw.mods.fml.relauncher.Side;
import de.take_weiland.mods.cameracraft.gui.ContainerPrinter;
import de.take_weiland.mods.commons.net.DataBuf;
import de.take_weiland.mods.commons.net.WritableDataBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class PacketPrinterGui extends CCPacket {

	private ContainerPrinter container;

	public PacketPrinterGui(ContainerPrinter container) {
		this.container = container;
	}

	@Override
	protected void write(WritableDataBuf buf) {
		buf.putByte(container.windowId);
		container.writeData(buf);
	}

	@Override
	protected void handle(DataBuf buf, EntityPlayer player, Side side) {
		int windowId = buf.getByte();
		Container c = player.openContainer;
		if (c.windowId == windowId && c instanceof ContainerPrinter) {
			((ContainerPrinter) c).readData(buf);
		}
	}

	@Override
	protected boolean validOn(Side side) {
		return side.isClient();
	}

}
