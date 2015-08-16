package de.take_weiland.mods.cameracraft.api.printer;

import java.util.Collection;

public interface Printer {

	/**
	 * @return whether this Printer can currently print
	 */
	boolean canPrint();
	
	/**
	 * @return whether this Printer currently can accept new jobs
	 */
	boolean acceptsJobs();
	
	/**
	 * add a PrintJob to the Queue
	 * @param job the Job to enqueue
	 * @return true if enqueuing was successful
	 */
	boolean addJob(PrintJob job);
	
	/**
	 * adds all the jobs in the given collection to the queue<br>
	 * The Collection may be truncated if it is too big for this Printer's queue
	 * @param jobs
	 * @return the amount of jobs actually added to the queue
	 */
	int addJobs(Iterable<? extends PrintJob> jobs);
	
	/**
	 * get this printer's job queue
	 * the collection is ordered from last Job to first (last element in the Collection is the next job to be executed)
	 * @return a non-null read-only view of this printer's Job queue
	 */
	Collection<QueuedPrintJob> getQueue();
	
	/**
	 * @return the current Job this printer is performing or null if there is currently no job being executed
	 */
	QueuedPrintJob getCurrentJob();
	
}
