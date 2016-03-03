package de.take_weiland.mods.cameracraft.network;

import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.commons.net.MCDataInput;
import de.take_weiland.mods.commons.net.MCDataOutput;
import de.take_weiland.mods.commons.net.Packet;

import java.util.concurrent.CompletionStage;

/**
 * @author diesieben07
 */
public class PacketRequestViewportPhoto implements Packet.WithResponse<PacketImageResponse> {

    private final int viewportId;

    public PacketRequestViewportPhoto(int viewportId) {
        this.viewportId = viewportId;
    }

    public PacketRequestViewportPhoto(MCDataInput in) {
        this.viewportId = in.readInt();
    }

    @Override
    public void writeTo(MCDataOutput out) throws Exception {
        out.writeInt(viewportId);
    }

    public CompletionStage<PacketImageResponse> handle() {
        return CameraCraft.proxy.handleViewportPhoto(viewportId);
    }

}
