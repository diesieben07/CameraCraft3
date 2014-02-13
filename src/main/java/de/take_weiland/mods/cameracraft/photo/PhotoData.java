package de.take_weiland.mods.cameracraft.photo;

import java.io.BufferedInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import org.apache.commons.io.IOUtils;

import de.take_weiland.mods.cameracraft.api.photo.Photo;
import de.take_weiland.mods.cameracraft.api.photo.TimeType;

public class PhotoData implements Photo {
	
	public static PhotoData fromFile(Integer id, File file) throws IOException {
		InputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(file));
			PhotoData data = fromInputStream(id, in);
			in.close();
			
			return data;
		} finally {
			IOUtils.closeQuietly(in);
		}
	}
	
	public static PhotoData fromInputStream(Integer id, InputStream in) throws IOException {
		return fromDataInput(id, new DataInputStream(in));
	}
	
	public static PhotoData fromDataInput(Integer id, DataInput in) throws IOException {
		String owner = in.readUTF();
		int timeType = in.readByte();
		if (timeType < 0) {
			return new PhotoData(id, owner);
		} else {
			TimeType[] timeTypes = TimeType.values();
			if (timeType < timeTypes.length) {
				long time = in.readLong();
				int x = in.readInt();
				int y = in.readInt();
				int z = in.readInt();
				int dimension = in.readInt();
				return new PhotoData(id, owner, time, timeTypes[timeType], dimension, x, y, z);
			} else {
				throw new IOException("Invalid TimeType in PhotoData file!");
			}
		}
	}
	
	private final Integer id;
	private final String owner;
	
	private final boolean hasTimeAndCoords;
	private long time;
	private TimeType timeType;
	
	private int dimension;
	private int x;
	private int y;
	private int z;
	
	private World world;
	private EntityPlayer player;
	
	public PhotoData(Integer id, String owner, long time, TimeType timeType, int dimension, int x, int y, int z) {
		this.id = id;
		this.owner = owner;
		this.time = time;
		this.timeType = timeType;
		this.dimension = dimension;
		this.x = x;
		this.y = y;
		this.z = z;
		hasTimeAndCoords = true;
	}

	public PhotoData(Integer id, String owner) {
		this.id = id;
		this.owner = owner;
		hasTimeAndCoords = false;
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public String getOwner() {
		return owner;
	}
	
	@Override
	public EntityPlayer getPlayerOwner() {
		return player != null ? player : (player = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(owner));
	}


	@Override
	public boolean hasLocationAndTime() {
		return hasTimeAndCoords;
	}
	
	private void checkTimeAndCoords() {
		if (!hasTimeAndCoords) {
			throw new IllegalStateException("Photo is not associated with time and location!");
		}
	}

	@Override
	public int getX() {
		checkTimeAndCoords();
		return x;
	}

	@Override
	public int getY() {
		checkTimeAndCoords();
		return y;
	}

	@Override
	public int getZ() {
		checkTimeAndCoords();
		return z;
	}

	@Override
	public int getDimension() {
		checkTimeAndCoords();
		return dimension;
	}

	@Override
	public World getWorld() {
		checkTimeAndCoords();
		return world != null ? world : (world = DimensionManager.getWorld(dimension));
	}

	@Override
	public TimeType getTimeType() {
		checkTimeAndCoords();
		return timeType;
	}

	@Override
	public long getTime() {
		checkTimeAndCoords();
		return time;
	}

}
