package de.take_weiland.mods.cameracraft.network;

import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.commons.net.MCDataInput;
import de.take_weiland.mods.commons.net.MCDataOutput;
import de.take_weiland.mods.commons.net.Packet;

/**
 * @author diesieben07
 */
public class PacketNewViewport implements Packet {

    private final int id;
    private final int dimension;
    private final double x, y, z;
    private final float pitch, yaw;

    public PacketNewViewport(int id, int dimension, double x, double y, double z, float pitch, float yaw) {
        this.id = id;
        this.dimension = dimension;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public PacketNewViewport(MCDataInput in) {
        this.id = in.readInt();
        this.dimension = in.readInt();
        this.x = in.readDouble();
        this.y = in.readDouble();
        this.z = in.readDouble();
        this.pitch = in.readFloat();
        this.yaw = in.readFloat();
    }

    @Override
    public void writeTo(MCDataOutput out) throws Exception {
        out.writeInt(id);
        out.writeInt(dimension);
        out.writeDouble(x);
        out.writeDouble(y);
        out.writeDouble(z);
        out.writeFloat(pitch);
        out.writeFloat(yaw);
    }

    public void handle() {
        CameraCraft.proxy.newViewport(id, dimension, x, y, z, pitch, yaw);
    }
}
