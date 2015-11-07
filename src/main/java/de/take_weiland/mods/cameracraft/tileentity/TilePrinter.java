package de.take_weiland.mods.cameracraft.tileentity;

import com.google.common.collect.Iterables;
import com.google.common.collect.Queues;
import com.google.common.primitives.Ints;
import de.take_weiland.mods.cameracraft.api.photo.PhotoStorage;
import de.take_weiland.mods.cameracraft.api.photo.PhotoStorageItem;
import de.take_weiland.mods.cameracraft.api.printer.InkItem;
import de.take_weiland.mods.cameracraft.api.printer.PrintJob;
import de.take_weiland.mods.cameracraft.api.printer.Printer;
import de.take_weiland.mods.cameracraft.api.printer.QueuedPrintJob;
import de.take_weiland.mods.cameracraft.blocks.CCBlock;
import de.take_weiland.mods.cameracraft.gui.ContainerPrinter;
import de.take_weiland.mods.cameracraft.item.CCItem;
import de.take_weiland.mods.cameracraft.item.PhotoType;
import de.take_weiland.mods.cameracraft.photo.SimplePrintJob;
import de.take_weiland.mods.commons.inv.Inventories;
import de.take_weiland.mods.commons.meta.HasSubtypes;
import de.take_weiland.mods.commons.nbt.NBT;
import de.take_weiland.mods.commons.net.Packets;
import de.take_weiland.mods.commons.sync.Sync;
import de.take_weiland.mods.commons.tileentity.TileEntityInventory;
import de.take_weiland.mods.commons.util.ItemStacks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.Queue;

public class TilePrinter extends TileEntityInventory implements ISidedInventory, Printer {

	private static final int MAX_QUEUE_SIZE = 100;
	public static final int SLOT_YELLOW = 0;
	public static final int SLOT_CYAN = 1;
	public static final int SLOT_MAGENTA = 2;
	public static final int SLOT_BLACK = 3;
	public static final int SLOT_PAPER = 4;
    public static final int SLOT_STORAGE = 5;
	
	private static final int JOB_TIME = 40;
	
	private Queue<SimplePrintJob.Queued> jobs = Queues.newArrayDeque();
	private SimplePrintJob.Queued currentJob;
	
	private int jobTimeout = 0;
	
	@Override
	public void updateEntity() {
		super.updateEntity();

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

    public int jobProgressClient;

	@Sync(inContainer = true)
	public int getJobProgress() {
        if (jobTimeout <= 0) {
            return 0;
        }
        int curjobDone = JOB_TIME - jobTimeout;
        return MathHelper.ceiling_double_int(ContainerPrinter.PROGRESSBAR_WIDTH * ((double) curjobDone / JOB_TIME));
	}

    private void setJobProgress(int progress) {
        jobProgressClient = progress;
    }
	
	private void executeJob(SimplePrintJob.Queued job) {
		Packet p = new S02PacketChat(new ChatComponentText("executing job: " + job.getPhotoId()));
		Packets.sendToAllTracking(p, this);

		job.decrease();
		ItemStack photo = CCItem.photo.getStack(PhotoType.PHOTO);
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
		Inventories.tryStore(photo, worldObj, xCoord, yCoord, zCoord, ForgeDirection.UP);
	}

	@Override
	public int getSizeInventory() {
		return 6;
	}

	public static final int[] INK_COLOR_TO_SLOT = new int[4];
	
	static {
		INK_COLOR_TO_SLOT[InkItem.Color.BLACK.ordinal()] = SLOT_BLACK;
		INK_COLOR_TO_SLOT[InkItem.Color.CYAN.ordinal()] = SLOT_CYAN;
		INK_COLOR_TO_SLOT[InkItem.Color.MAGENTA.ordinal()] = SLOT_MAGENTA;
		INK_COLOR_TO_SLOT[InkItem.Color.YELLOW.ordinal()] = SLOT_YELLOW;
	}
	
    @SuppressWarnings("SimplifiableIfStatement")
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		Item item;
		if (slot == SLOT_PAPER) {
            return ItemStacks.is(stack, Items.paper);
        } else if (slot == SLOT_STORAGE) {
            return stack.getItem() instanceof PhotoStorageItem;
		} else if ((item = stack.getItem()) instanceof InkItem) {
			return INK_COLOR_TO_SLOT[((InkItem) item).getColor(stack).ordinal()] == slot;
		} else {
            return false;
        }
	}
	
	private static final int[] extractSlots = new int[] { }; // TODO
	private static final int[] paperSlot = new int[] { SLOT_PAPER };
	private static final int[] inkSlots = new int[] { SLOT_YELLOW, SLOT_CYAN, SLOT_MAGENTA, SLOT_BLACK };
	
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
	public String getDefaultName() {
        return HasSubtypes.name(CCBlock.machines, CCBlock.machines.getType(getBlockMetadata()));
	}
	
	static NBTTagCompound encodeJob(SimplePrintJob.Queued job) {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setLong("id", job.getPhotoId());
		nbt.setInteger("amnt", (short) job.getAmount());
		nbt.setInteger("amntLeft", job.getRemainingAmount());
		return nbt;
	}
	
	static SimplePrintJob.Queued decodeJob(NBTTagCompound nbt) {
		return new SimplePrintJob.Queued(nbt.getLong("id"), nbt.getInteger("amnt"), nbt.getInteger("amntLeft"));
	}

    @Override
	public void writeToNBT(@Nonnull NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		NBTTagList encJobs = new NBTTagList();
        for (SimplePrintJob.Queued job : jobs) {
            encJobs.appendTag(encodeJob(job));
        }

		nbt.setTag("jobs", encJobs);
		
		if (currentJob != null) {
			nbt.setTag("curJob", encodeJob(currentJob));
		}
		
		nbt.setByte("timeout", (byte) jobTimeout);
	}
	
	@Override
	public void readFromNBT(@Nonnull NBTTagCompound nbt) {
		super.readFromNBT(nbt);

        for (NBTTagCompound compound : NBT.<NBTTagCompound>asList(nbt.getTagList("jobs", NBT.TAG_COMPOUND))) {
            jobs.add(decodeJob(compound));
        }

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

	@Override
	public Collection<QueuedPrintJob> getQueue() {
        return Collections.unmodifiableCollection(jobs);
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
	public int addJobs(Iterable<? extends PrintJob> toAdd) {
        int appendLen = MAX_QUEUE_SIZE - jobs.size();
        for (PrintJob job : Iterables.limit(toAdd, appendLen)) {
            jobs.add(SimplePrintJob.makeQueued(job));
        }

        return appendLen;
	}

	public PhotoStorage getStorage() {
		ItemStack stack = getStackInSlot(SLOT_STORAGE);
        if (stack != null && stack.getItem() instanceof PhotoStorageItem) {
            return ((PhotoStorageItem) stack.getItem()).getPhotoStorage(stack);
        } else {
            return null;
        }
	}

	@Override
	public int[] getSlotsForFace(int side) {
		return accessibleSlots0(ForgeDirection.VALID_DIRECTIONS[side]);
	}

}
