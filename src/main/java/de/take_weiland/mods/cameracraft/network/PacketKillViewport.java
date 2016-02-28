package de.take_weiland.mods.cameracraft.network;

import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.commons.net.MCDataInput;
import de.take_weiland.mods.commons.net.MCDataOutput;
import de.take_weiland.mods.commons.net.Packet;

/**
 * @author diesieben07
 */
public class PacketKillViewport implements Packet {

    private final int id;

    public PacketKillViewport(int id) {
        this.id = id;
    }

    public PacketKillViewport(MCDataInput in) {
        this.id = in.readInt();
    }

    @Override
    public void writeTo(MCDataOutput out) throws Exception {
        out.writeInt(id);
    }

    public void handle() {
        CameraCraft.proxy.killViewport(id);
    }
}
