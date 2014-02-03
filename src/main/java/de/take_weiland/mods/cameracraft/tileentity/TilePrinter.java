package de.take_weiland.mods.cameracraft.tileentity;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Queue;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet3Chat;
import net.minecraft.util.ChatMessageComponent;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterators;
import com.google.common.collect.Queues;

import de.take_weiland.mods.cameracraft.api.cable.NetworkNode;
import de.take_weiland.mods.cameracraft.api.cable.NetworkTile;
import de.take_weiland.mods.cameracraft.api.printer.PrintJob;
import de.take_weiland.mods.cameracraft.api.printer.Printer;
import de.take_weiland.mods.cameracraft.api.printer.QueuedPrintJob;
import de.take_weiland.mods.cameracraft.blocks.MachineType;
import de.take_weiland.mods.cameracraft.item.CCItem;
import de.take_weiland.mods.cameracraft.item.PhotoType;
import de.take_weiland.mods.cameracraft.networking.NetworkNodeImpl;
import de.take_weiland.mods.cameracraft.photo.SimplePrintJob;
import de.take_weiland.mods.commons.net.Packets;
import de.take_weiland.mods.commons.templates.TileEntityInventory;
import de.take_weiland.mods.commons.util.ItemStacks;
import de.take_weiland.mods.commons.util.Multitypes;
import de.take_weiland.mods.commons.util.NBT;
import de.take_weiland.mods.commons.util.UnsignedShorts;

public class TilePrinter extends TileEntityInventory<TilePrinter> implements NetworkTile, Printer {

	private static final int MAX_QUEUE_SIZE = 100;
	public static final int SLOT_YELLOW = 0;
	public static final int SLOT_CYAN = 1;
	public static final int SLOT_MAGENTA = 2;
	public static final int SLOT_BLACK = 3;
	public static final int SLOT_PAPER = 4;
	
	private static final int JOB_TIME = 40;
	
	private NetworkNodeImpl node = new NetworkNodeImpl(this);
	private Queue<SimplePrintJob.Queued> jobs = Queues.newArrayDeque();
	private SimplePrintJob.Queued currentJob;
	
	private int jobTimeout = 0;
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		node.update();
		
		if (jobTimeout == 0) {
			if (currentJob != null) {
				executeJob(currentJob);
				if (currentJob.isFinished()) {
					currentJob = null;
				}
			}
			if (currentJob == null) {
				currentJob = jobs.poll();
			}
			jobTimeout = JOB_TIME;
		}
		jobTimeout--;
	}
	
	private void executeJob(SimplePrintJob.Queued job) {
		Packet p = new Packet3Chat(ChatMessageComponent.createFromText("executing job: " + job.getPhotoId()));
		Packets.sendPacketToAllTracking(p, this);
		
		job.decrease();
		ItemStack photo = ItemStacks.of(PhotoType.PHOTO);
		CCItem.photo.setPhotoId(photo, job.getPhotoId());
		
		worldObj.spawnEntityInWorld(new EntityItem(worldObj, xCoord, yCoord + 1, zCoord, photo));
	}

	public int getSizeInventory() {
		return 5;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack item) {
		if (ItemStacks.is(item, CCItem.miscItems)) {
			return Multitypes.getType(CCItem.miscItems, item).ordinal() - 1 == slot;
		} else {
			return slot == SLOT_PAPER && ItemStacks.is(item, Item.paper);
		}
	}

	@Override
	protected String getDefaultName() {
		return Multitypes.fullName(MachineType.PRINTER);
	}
	
	@Override
	public void invalidate() {
		super.invalidate();
		node.shutdown();
	}

	@Override
	public String getNetworkName() {
		return "Printer [x=" + xCoord + ", y=" + yCoord + ", z=" + zCoord + "]";
	}

	@Override
	public NetworkNode getNode() {
		return node;
	}
	
	static NBTTagCompound encodeJob(SimplePrintJob.Queued job) {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("id", job.getPhotoId());
		nbt.setShort("amnt", (short) job.getAmount());
		nbt.setShort("amntLeft", UnsignedShorts.checkedCast(job.getRemainingAmount()));
		return nbt;
	}
	
	static SimplePrintJob.Queued decodeJob(NBTTagCompound nbt) {
		return new SimplePrintJob.Queued(nbt.getString("id"), nbt.getShort("amnt"), nbt.getShort("amntLeft"));
	}

	private static final Function<NBTTagCompound, SimplePrintJob.Queued> JOB_DECODER = new Function<NBTTagCompound, SimplePrintJob.Queued>() {

		@Override
		public SimplePrintJob.Queued apply(NBTTagCompound input) {
			return decodeJob(input);
		}
		
	};
	
	private static final Function<SimplePrintJob.Queued, NBTTagCompound> JOB_ENCODER = new Function<SimplePrintJob.Queued, NBTTagCompound>() {

		@Override
		public NBTTagCompound apply(SimplePrintJob.Queued input) {
			return encodeJob(input);
		}
	};
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		NBTTagList encJobs = new NBTTagList();
		NBT.asList(encJobs).addAll(Collections2.transform(jobs, JOB_ENCODER));
		
		nbt.setTag("jobs", encJobs);
		
		if (currentJob != null) {
			nbt.setCompoundTag("curJob", encodeJob(currentJob));
		}
		
		nbt.setByte("timeout", (byte) jobTimeout);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		jobs.addAll(Collections2.transform(NBT.<NBTTagCompound>asList(nbt.getTagList("jobs")), JOB_DECODER));
		if (nbt.hasKey("curJob")) {
			currentJob = decodeJob(nbt.getCompoundTag("curJob"));
		}
		
		jobTimeout = nbt.getByte("timeout");
	}

	// Printer API
	
	@Override
	public boolean canPrint() {
		return true;
	}

	@Override
	public boolean addJob(PrintJob job) {
		if (acceptsJobs()) {
			jobs.add(SimplePrintJob.makeQueued(job));
			return true;
		}
		return false;
	}

	private Collection<QueuedPrintJob> immutableQueue;
	
	@Override
	public Collection<QueuedPrintJob> getQueue() {
		return immutableQueue == null ? (immutableQueue = Collections.<QueuedPrintJob>unmodifiableCollection(jobs)) : immutableQueue;
	}

	@Override
	public QueuedPrintJob getCurrentJob() {
		return currentJob;
	}

	@Override
	public boolean acceptsJobs() {
		return jobs.size() < MAX_QUEUE_SIZE;
	}

	@Override
	public int addJobs(Collection<? extends PrintJob> toAdd) {
		int queueLen = this.jobs.size();
		if (queueLen >= MAX_QUEUE_SIZE) {
			return 0;
		}
		
		Iterator<SimplePrintJob.Queued> transformed = Iterators.transform(toAdd.iterator(), ToQueuedFunction.INSTANCE);
		int len = toAdd.size();
		if (len + queueLen <= MAX_QUEUE_SIZE) {
			Iterators.addAll(this.jobs, transformed);
			return len;
		}
		int appendLen = MAX_QUEUE_SIZE - queueLen;
		Iterators.addAll(this.jobs, Iterators.limit(transformed, appendLen));
		return appendLen;
	}
	
	private static enum ToQueuedFunction implements Function<PrintJob, SimplePrintJob.Queued> {
		INSTANCE;

		@Override
		public SimplePrintJob.Queued apply(PrintJob input) {
			return SimplePrintJob.makeQueued(input);
		}
	}

}
