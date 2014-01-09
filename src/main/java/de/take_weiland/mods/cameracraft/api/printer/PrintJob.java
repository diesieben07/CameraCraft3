package de.take_weiland.mods.cameracraft.api.printer;

import com.google.common.base.Preconditions;

public final class PrintJob {

	private final String photoId;
	private final int amount;
	
	public PrintJob(String photoId) {
		this(photoId, 1);
	}
	
	public PrintJob(String photoId, int amount) {
		this.photoId = Preconditions.checkNotNull(photoId);
		this.amount = amount;
	}

	public String getPhotoId() {
		return photoId;
	}

	public int getAmount() {
		return amount;
	}

}
