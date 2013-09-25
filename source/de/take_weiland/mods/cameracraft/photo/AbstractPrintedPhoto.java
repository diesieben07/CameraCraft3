package de.take_weiland.mods.cameracraft.photo;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import de.take_weiland.mods.cameracraft.api.camera.PrintedPhoto;
import de.take_weiland.mods.cameracraft.api.camera.TimeType;

public abstract class AbstractPrintedPhoto implements PrintedPhoto {

	protected final String id;
	private PhotoData delegate;
	
	protected AbstractPrintedPhoto(String id) {
		this.id = id;
	}

	@Override
	public final String getId() {
		return id;
	}
	
	private void setupDelegate() {
		if (delegate != null) {
			delegate = PhotoDataManager.getDataForId(id);
		}
	}

	@Override
	public final String getOwner() {
		setupDelegate();
		return delegate.getOwner();
	}

	@Override
	public final EntityPlayer getPlayerOwner() {
		setupDelegate();
		return delegate.getPlayerOwner();
	}

	@Override
	public final boolean hasLocationAndTime() {
		setupDelegate();
		return delegate.hasLocationAndTime();
	}

	@Override
	public final int getX() {
		setupDelegate();
		return delegate.getX();
	}

	@Override
	public final int getY() {
		setupDelegate();
		return delegate.getY();
	}

	@Override
	public final int getZ() {
		setupDelegate();
		return delegate.getZ();
	}

	@Override
	public final int getDimension() {
		setupDelegate();
		return delegate.getDimension();
	}

	@Override
	public final World getWorld() {
		setupDelegate();
		return delegate.getWorld();
	}

	@Override
	public TimeType getTimeType() {
		setupDelegate();
		return delegate.getTimeType();
	}

	@Override
	public final long getTime() {
		setupDelegate();
		return delegate.getTime();
	}

}
