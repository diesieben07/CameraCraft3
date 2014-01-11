package de.take_weiland.mods.cameracraft.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import cpw.mods.fml.relauncher.Side;
import de.take_weiland.mods.cameracraft.api.printer.PrintJob;
import de.take_weiland.mods.cameracraft.gui.ContainerPrinter;
import de.take_weiland.mods.cameracraft.photo.PhotoManager;
import de.take_weiland.mods.commons.network.DataPacket;
import de.take_weiland.mods.commons.network.PacketType;

public class PacketPrintJob extends DataPacket {

	private int windowId;
	private PrintJob job;
	
	public PacketPrintJob(Container c, PrintJob job) {
		this.windowId = c.windowId;
		this.job = job;
	}

	@Override
	protected void write(DataOutputStream out) throws IOException {
		out.writeByte(windowId);
		out.writeInt(PhotoManager.asInt(job.getPhotoId()));
		out.writeShort(job.getAmount());
	}
	
	@Override
	protected void read(EntityPlayer player, Side side, DataInputStream in) throws IOException {
		windowId = in.readByte();
		job = new PrintJob(PhotoManager.asString(in.readInt()), in.readUnsignedShort());
	}
	
	@Override
	public void execute(EntityPlayer player, Side side) {
		if (player.openContainer.windowId == windowId && player.openContainer instanceof ContainerPrinter) {
			((ContainerPrinter) player.openContainer).inventory().addJob(job);
		}
	}
	
	@Override
	public boolean isValidForSide(Side side) {
		return side.isServer();
	}

	@Override
	public PacketType type() {
		return CCPackets.PRINT_JOB;
	}

}
