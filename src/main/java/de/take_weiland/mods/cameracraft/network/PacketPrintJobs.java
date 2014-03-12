package de.take_weiland.mods.cameracraft.network;

import cpw.mods.fml.relauncher.Side;
import de.take_weiland.mods.cameracraft.gui.ContainerPrinter;
import de.take_weiland.mods.cameracraft.photo.SimplePrintJob;
import de.take_weiland.mods.commons.net.DataBuf;
import de.take_weiland.mods.commons.net.ModPacket;
import de.take_weiland.mods.commons.net.WritableDataBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

import java.util.Arrays;
import java.util.Collection;

public class PacketPrintJobs extends ModPacket {

	private int windowId;
	private Collection<SimplePrintJob> jobs;
	
	public PacketPrintJobs(Container c, Collection<SimplePrintJob> jobs) {
		this.windowId = c.windowId;
		this.jobs = jobs;
	}

	@Override
	protected void write(WritableDataBuf buffer) {
		buffer.putByte((byte) windowId);
		buffer.putVarInt(jobs.size());
		for (SimplePrintJob job : jobs) {
			buffer.putVarInt(job.getPhotoId().intValue());
			buffer.putVarInt(job.getAmount());
		}
	}

	@Override
	protected void handle(DataBuf buf, EntityPlayer player, Side side) {
		windowId = buf.getByte();
		if (player.openContainer.windowId == windowId && player.openContainer instanceof ContainerPrinter) {
			int len = buf.getVarInt();
			SimplePrintJob[] jobs = new SimplePrintJob[len];
			for (int i = 0; i < len; ++i) {
				jobs[i] = new SimplePrintJob(Integer.valueOf(buf.getVarInt()), buf.getVarInt());
			}
			((ContainerPrinter) player.openContainer).inventory().addJobs(Arrays.asList(jobs));
		}
	}
	
	@Override
	protected boolean validOn(Side side) {
		return side.isServer();
	}

}
