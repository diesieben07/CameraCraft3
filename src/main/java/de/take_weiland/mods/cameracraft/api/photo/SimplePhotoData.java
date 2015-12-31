package de.take_weiland.mods.cameracraft.api.photo;

import de.take_weiland.mods.cameracraft.api.owner.Owner;

/**
 * <p>Simple immutable implementation of {@link PhotoData}.</p>
 *
 * @author diesieben07
 */
public final class SimplePhotoData implements PhotoData {

    private final Owner owner;
    private final double  x, y, z;
    private final int  dim;
    private final long inGameTime, unixTime;

    public SimplePhotoData(Owner owner, double x, double y, double z, int dim, long inGameTime, long unixTime) {
        this.owner = owner;
        this.x = x;
        this.y = y;
        this.z = z;
        this.dim = dim;
        this.inGameTime = inGameTime;
        this.unixTime = unixTime;
    }

    @Override
    public Owner getOwner() {
        return owner;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public double getZ() {
        return z;
    }

    @Override
    public int getDimension() {
        return dim;
    }

    @Override
    public long getTime() {
        return inGameTime;
    }

    @Override
    public long getUnixTime() {
        return unixTime;
    }
}
