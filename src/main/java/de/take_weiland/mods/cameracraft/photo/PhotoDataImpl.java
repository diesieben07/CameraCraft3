package de.take_weiland.mods.cameracraft.photo;

import de.take_weiland.mods.cameracraft.api.photo.PhotoData;
import de.take_weiland.mods.cameracraft.api.photo.TimeType;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.UUID;

public class PhotoDataImpl implements PhotoData {
	
	private final long id;
	private final UUID owner;
	
	private final long time;
	private final TimeType timeType;
	
	private final int dimension;
	private final int x;
	private final int y;
	private final int z;
	
	public PhotoDataImpl(long id, UUID owner, long time, TimeType timeType, int dimension, int x, int y, int z) {
		this.id = id;
		this.owner = owner;
		this.time = time;
		this.timeType = timeType;
		this.dimension = dimension;
		this.x = x;
		this.y = y;
		this.z = z;
	}

    @Override
    public long getId() {
        return id;
    }

    @Override
    public UUID getOwner() {
        return owner;
    }

    @Override
	public int getX() {
        return x;
	}

	@Override
	public int getY() {
        return y;
	}

	@Override
	public int getZ() {
        return z;
	}

	@Override
	public int getDimension() {
        return dimension;
	}

    @Override
	public TimeType getTimeType() {
        return timeType;
	}

	@Override
	public long getTime() {
        return time;
	}

	public static PhotoDataImpl load(long id, DataInput in) throws IOException {
		long lsb = in.readLong();
		long msb = in.readLong();
		UUID owner = new UUID(msb, lsb);
        long time = in.readLong();
        int dim = in.readInt();
        int x = in.readInt();
        int y = in.readInt();
        int z = in.readInt();
        return new PhotoDataImpl(id, owner, time, TimeType.INGAME, dim, x, y, z);
	}

    public static void write(PhotoData data, DataOutput out) throws IOException {
        UUID owner = data.getOwner();
        out.writeLong(owner.getLeastSignificantBits());
        out.writeLong(owner.getMostSignificantBits());
        out.writeLong(data.getTime());
        out.writeInt(data.getDimension());
        out.writeInt(data.getX());
        out.writeInt(data.getY());
        out.writeInt(data.getZ());
    }
}
