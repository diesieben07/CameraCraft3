package de.take_weiland.mods.cameracraft.photo;

import com.google.common.base.Preconditions;

import de.take_weiland.mods.cameracraft.api.printer.PrintJob;
import de.take_weiland.mods.cameracraft.api.printer.QueuedPrintJob;

public final class SimplePrintJob implements PrintJob {

	private final Integer photoId;
	private final int amount;
	
	public SimplePrintJob(Integer photoId) {
		this(photoId, 1);
	}
	
	public SimplePrintJob(Integer photoId, int amount) {
		this.photoId = Preconditions.checkNotNull(photoId);
		this.amount = amount;
	}

	@Override
	public Integer getPhotoId() {
		return photoId;
	}
	
	@Override
	public int getAmount() {
		return amount;
	}
	
	public SimplePrintJob.Queued makeQueued() {
		return new SimplePrintJob.Queued(photoId, amount);
	}
	
	public static SimplePrintJob.Queued makeQueued(PrintJob job) {
		return new SimplePrintJob.Queued(job.getPhotoId(), job.getAmount());
	}
	
	public static class Queued implements QueuedPrintJob {

		private final Integer photoId;
		private final int amount;
		private int amountLeft;
		
		public Queued(Integer photoId, int amount) {
			this(photoId, amount, amount);
		}
		
		public Queued(Integer photoId, int amount, int amountLeft) {
			this.photoId = photoId;
			this.amount = amount;
			this.amountLeft = amountLeft;
		}

		@Override
		public Integer getPhotoId() {
			return photoId;
		}

		@Override
		public int getAmount() {
			return amount;
		}

		@Override
		public int getRemainingAmount() {
			return amountLeft;
		}
		
		@Override
		public boolean isFinished() {
			return amountLeft == 0;
		}
		
		public void decrease() {
			amountLeft--;
		}
	}

}
