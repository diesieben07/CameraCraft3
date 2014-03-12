package de.take_weiland.mods.cameracraft.tileentity;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterators;
import com.google.common.collect.Queues;
import com.google.common.primitives.Ints;
import de.take_weiland.mods.cameracraft.api.cable.NetworkNode;
import de.take_weiland.mods.cameracraft.api.cable.NetworkTile;
import de.take_weiland.mods.cameracraft.api.printer.InkItem;
import de.take_weiland.mods.cameracraft.api.printer.PrintJob;
import de.take_weiland.mods.cameracraft.api.printer.Printer;
import de.take_weiland.mods.cameracraft.api.printer.QueuedPrintJob;
import de.take_weiland.mods.cameracraft.blocks.CCBlock;
import de.take_weiland.mods.cameracraft.item.CCItem;
import de.take_weiland.mods.cameracraft.item.PhotoType;
import de.take_weiland.mods.cameracraft.networking.NetworkNodeImpl;
import de.take_weiland.mods.cameracraft.photo.SimplePrintJob;
import de.take_weiland.mods.commons.net.Packets;
import de.take_weiland.mods.commons.tileentity.TileEntityInventory;
import de.take_weiland.mods.commons.util.ItemStacks;
import de.take_weiland.mods.commons.util.Multitypes;
import de.take_weiland.mods.commons.util.NBT;
import de.take_weiland.mods.commons.util.UnsignedShorts;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet3Chat;
import net.minecraft.util.ChatMessageComponent;
import net.minecraftforge.common.ForgeDirection;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Queue;

public class TilePrinter extends TileEntityInventory<TilePrinter> implements ISidedInventory, NetworkTile, Printer {

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
		
		if (canPrint()) { // TODO: maybe don't check every tick?
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
				if (currentJob != null) {
					jobTimeout = JOB_TIME;
				}
			}
			if (jobTimeout > 0) {
				jobTimeout--;
			}
		}
	}
	
	private void executeJob(SimplePrintJob.Queued job) {
		Packet p = new Packet3Chat(ChatMessageComponent.createFromText("executing job: " + job.getPhotoId()));
		Packets.sendPacketToAllTracking(p, this);
		
		job.decrease();
		ItemStack photo = ItemStacks.of(PhotoType.PHOTO);
		CCItem.photo.setPhotoId(photo, job.getPhotoId());
		
		useSupplies();
		tryStore(photo);
	}

	private void useSupplies() {
		for (int slot : inkSlots) {
			ItemStack stack = getStackInSlot(slot);
			InkItem ink = (InkItem) stack.getItem();
			ink.setAmount(stack, ink.getAmount(stack) - 10);
		}
		decrStackSize(SLOT_PAPER, 1);
	}

	private void tryStore(ItemStack photo) {
		// TODO
		worldObj.spawnEntityInWorld(new EntityItem(worldObj, xCoord, yCoord + 1, zCoord, photo));
	}

	public int getSizeInventory() {
		return 5;
	}

	public static final int[] INK_COLOR_TO_SLOT = new int[4];
	
	static {
		INK_COLOR_TO_SLOT[InkItem.Color.BLACK.ordinal()] = SLOT_BLACK;
		INK_COLOR_TO_SLOT[InkItem.Color.CYAN.ordinal()] = SLOT_CYAN;
		INK_COLOR_TO_SLOT[InkItem.Color.MAGENTA.ordinal()] = SLOT_MAGENTA;
		INK_COLOR_TO_SLOT[InkItem.Color.YELLOW.ordinal()] = SLOT_YELLOW;
	}
	
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		Item item;
		if (slot == SLOT_PAPER) {
			ItemStacks.is(stack, Item.paper);
		} else if ((item = stack.getItem()) instanceof InkItem) {
			return INK_COLOR_TO_SLOT[((InkItem) item).getColor(stack).ordinal()] == slot;
		}
		return false;
	}
	
	private static final int[] extractSlots = new int[] { }; // TODO
	private static final int[] paperSlot = new int[] { SLOT_PAPER };
	private static final int[] inkSlots = new int[] { SLOT_YELLOW, SLOT_CYAN, SLOT_MAGENTA, SLOT_BLACK };

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return accessibleSlots0(ForgeDirection.VALID_DIRECTIONS[side]);
	}
	
	private int[] accessibleSlots0(ForgeDirection side) {
		switch (side) {
		case DOWN:
			return extractSlots;
		case UP:
			return paperSlot;
		default:
			return inkSlots;
		}
	}
	
	private boolean isStackValidOn(ItemStack stack, ForgeDirection dir, int slot) {
		return Ints.contains(accessibleSlots0(dir), slot) && isItemValidForSlot(slot, stack);
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[side];
		return dir != ForgeDirection.DOWN // can't insert from below
				&& isStackValidOn(stack, dir, slot); // TODO: this should ALWAYS be true, based on when vanilla calls this. optimize?
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[side];
		return dir == ForgeDirection.DOWN
				&& Ints.contains(accessibleSlots0(dir), slot); // see above, should always be true
	}

	@Override
	protected String unlocalizedName() {
		return Multitypes.fullName(Multitypes.getType(CCBlock.machines, getBlockMetadata()));
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
		nbt.setInteger("id", job.getPhotoId().intValue());
		nbt.setShort("amnt", (short) job.getAmount());
		nbt.setShort("amntLeft", UnsignedShorts.checkedCast(job.getRemainingAmount()));
		return nbt;
	}
	
	static SimplePrintJob.Queued decodeJob(NBTTagCompound nbt) {
		return new SimplePrintJob.Queued(Integer.valueOf(nbt.getInteger("id")), nbt.getShort("amnt"), nbt.getShort("amntLeft"));
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
		if (getStackInSlot(SLOT_PAPER) == null) {
			return false;
		}
		for (int slot : inkSlots) {
			ItemStack stack;
			if ((stack = getStackInSlot(slot)) == null || !isItemValidForSlot(slot, stack)) {
				return false;
			}
		}
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
