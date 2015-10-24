package de.take_weiland.mods.cameracraft.network;

import cpw.mods.fml.relauncher.Side;
import de.take_weiland.mods.cameracraft.gui.ContainerPrinter;
import de.take_weiland.mods.cameracraft.photo.SimplePrintJob;
import de.take_weiland.mods.commons.net.MCDataInput;
import de.take_weiland.mods.commons.net.MCDataOutput;
import de.take_weiland.mods.commons.net.Packet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

import java.util.Arrays;
import java.util.Collection;

@Packet.Receiver(Side.SERVER)
public class PacketPrintJobs implements Packet {

	private final int windowId;
	private final Collection<SimplePrintJob> jobs;
	
	public PacketPrintJobs(Container c, Collection<SimplePrintJob> jobs) {
		this.windowId = c.windowId;
		this.jobs = jobs;
	}

	public PacketPrintJobs(MCDataInput in) {
        windowId = in.readByte();
        int len = in.readVarInt();
        SimplePrintJob[] jobs = new SimplePrintJob[len];
        for (int i = 0; i < len; ++i) {
            jobs[i] = new SimplePrintJob(in.readInt(), in.readVarInt());
        }
        this.jobs = Arrays.asList(jobs);
    }

	@Override
    public void writeTo(MCDataOutput out) {
		out.writeByte((byte) windowId);
		out.writeVarInt(jobs.size());
		for (SimplePrintJob job : jobs) {
			out.writeLong(job.getPhotoId());
			out.writeVarInt(job.getAmount());
		}
	}

    public void handle(EntityPlayer player) {
		if (player.openContainer.windowId == windowId && player.openContainer instanceof ContainerPrinter) {
			((ContainerPrinter) player.openContainer).inventory().addJobs(jobs);
		}
	}
	
}
