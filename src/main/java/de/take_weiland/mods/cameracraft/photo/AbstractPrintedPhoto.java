package de.take_weiland.mods.cameracraft.photo;

import de.take_weiland.mods.cameracraft.api.photo.PrintedPhoto;

public abstract class AbstractPrintedPhoto implements PrintedPhoto {

	protected final long id;

	protected AbstractPrintedPhoto(long id) {
		this.id = id;
	}

	@Override
	public final long getPhotoId() {
		return id;
	}
	
}
