package de.take_weiland.mods.cameracraft.api.printer;

public interface QueuedPrintJob extends PrintJob {

	int getRemainingAmount();
	
	boolean isFinished();
	
}
