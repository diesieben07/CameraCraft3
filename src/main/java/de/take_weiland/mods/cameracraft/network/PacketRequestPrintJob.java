package de.take_weiland.mods.cameracraft.network;

import de.take_weiland.mods.cameracraft.gui.ContainerPrinter;
import de.take_weiland.mods.cameracraft.photo.SimplePrintJob;
import de.take_weiland.mods.commons.net.MCDataInput;
import de.take_weiland.mods.commons.net.MCDataOutput;
import de.take_weiland.mods.commons.net.Packet;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author diesieben07
 */
public class PacketRequestPrintJob implements Packet {

    private final int windowId;
    private final long photoId;
    private final int amount;

    public PacketRequestPrintJob(int windowId, long photoId, int amount) {
        this.windowId = windowId;
        this.photoId = photoId;
        this.amount = amount;
    }

    public PacketRequestPrintJob(MCDataInput in) {
        windowId = in.readByte();
        photoId = in.readLong();
        amount = in.readVarInt();
    }

    @Override
    public void writeTo(MCDataOutput out) {
        out.writeByte(windowId);
        out.writeLong(photoId);
        out.writeVarInt(amount);
    }

    public void handle(EntityPlayer player) {
        if (player.openContainer.windowId == windowId && player.openContainer instanceof ContainerPrinter) {
            // TODO check for flooding
            ((ContainerPrinter) player.openContainer).inventory().addJob(new SimplePrintJob(photoId, amount));
        }
    }

}