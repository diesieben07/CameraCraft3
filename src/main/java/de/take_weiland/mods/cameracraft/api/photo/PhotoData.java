package de.take_weiland.mods.cameracraft.api.photo;

import de.take_weiland.mods.cameracraft.api.owner.Owner;
import net.minecraft.world.World;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * <p>Represents data about a Photo.</p>
 *
 * @author diesieben07
 */
public interface PhotoData {

    /**
     * <p>The owner of this photo. The owner is the creator of the photo.</p>
     *
     * @return the owner
     */
    Owner getOwner();

    /**
     * <p>The x coordinate this photo was taken at.</p>
     *
     * @return the x coordinate
     */
    double getX();

    /**
     * <p>The y coordinate this photo was taken at.</p>
     *
     * @return the y coordinate
     */
    double getY();

    /**
     * <p>The z coordinate this photo was taken at.</p>
     *
     * @return the z coordinate
     */
    double getZ();

    /**
     * <p>The ID of the dimension this photo was taken in.</p>
     *
     * @return the dimension ID
     */
    int getDimension();

    /**
     * <p>The in-game time this photo was taken at as returned by {@link World#getTotalWorldTime()}.</p>
     *
     * @return the in-game time
     */
    long getTime();

    /**
     * <p>Get the unix timestamp this photo was taken at.</p>
     *
     * @return the real-world time
     */
    long getUnixTime();

    default void write(DataOutput out) throws IOException {
        getOwner().write(out);
        out.writeDouble(getX());
        out.writeDouble(getY());
        out.writeDouble(getZ());
        out.writeInt(getDimension());
        out.writeLong(getTime());
        out.writeLong(getUnixTime());
    }

    static PhotoData read(DataInput in) throws IOException {
        Owner owner = Owner.read(in);
        double x = in.readDouble();
        double y = in.readDouble();
        double z = in.readDouble();
        int dim = in.readInt();
        long time = in.readLong();
        long unix = in.readLong();
        return new SimplePhotoData(owner, x, y, z, dim, time, unix);
    }

}
