package de.take_weiland.mods.cameracraft.tileentity;

import java.util.Collection;
import java.util.Collections;
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
import com.google.common.base.Preconditions;
import com.google.common.collect.Collections2;
import com.google.common.collect.Queues;

import cpw.mods.fml.common.network.PacketDispatcher;
import de.take_weiland.mods.cameracraft.api.cable.NetworkNode;
import de.take_weiland.mods.cameracraft.api.cable.NetworkTile;
import de.take_weiland.mods.cameracraft.api.printer.PrintJob;
import de.take_weiland.mods.cameracraft.api.printer.Printer;
import de.take_weiland.mods.cameracraft.blocks.MachineType;
import de.take_weiland.mods.cameracraft.item.CCItem;
import de.take_weiland.mods.cameracraft.item.ItemPhoto;
import de.take_weiland.mods.cameracraft.item.PhotoType;
import de.take_weiland.mods.cameracraft.networking.NetworkNodeImpl;
import de.take_weiland.mods.commons.templates.TileEntityInventory;
import de.take_weiland.mods.commons.util.ItemStacks;
import de.take_weiland.mods.commons.util.Multitypes;
import de.take_weiland.mods.commons.util.NBT;

public class TilePrinter extends TileEntityInventory<TilePrinter> implements NetworkTile, Printer {

	public static final int SLOT_YELLOW = 0;
	public static final int SLOT_CYAN = 1;
	public static final int SLOT_MAGENTA = 2;
	public static final int SLOT_BLACK = 3;
	public static final int SLOT_PAPER = 4;
	
	private static final int JOB_TIME = 40;
	
	private NetworkNodeImpl node = new NetworkNodeImpl(this);
	private Queue<PrintJob> jobs = Queues.newArrayDeque();
	private PrintJob currentJob;
	
	private int jobTimeout = 0;
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		node.update();
		
		if (jobTimeout == 0) {
			if (currentJob != null) {
				executeJob(currentJob);
			}
			currentJob = jobs.poll();
			jobTimeout = JOB_TIME;
		}
		jobTimeout--;
	}
	
	private void executeJob(PrintJob job) {
		Packet p = new Packet3Chat(ChatMessageComponent.createFromText("executing job: " + job.getPhotoId() + " x " + job.getAmount()));
		PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 20, worldObj.provider.dimensionId, p);
		
		for (int i = 0; i < job.getAmount(); ++i) {
			ItemStack photo = ItemStacks.of(PhotoType.PHOTO);
			CCItem.photo.setPhotoId(photo, job.getPhotoId());
			worldObj.spawnEntityInWorld(new EntityItem(worldObj, xCoord, yCoord + 1, zCoord, photo));
		}
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
	
	static NBTTagCompound encodeJob(PrintJob job) {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("id", job.getPhotoId());
		nbt.setShort("amnt", (short) job.getAmount());
		return nbt;
	}
	
	static PrintJob decodeJob(NBTTagCompound nbt) {
		return new PrintJob(nbt.getString("id"), nbt.getShort("amnt"));
	}

	private static final Function<NBTTagCompound, PrintJob> JOB_DECODER = new Function<NBTTagCompound, PrintJob>() {

		@Override
		public PrintJob apply(NBTTagCompound input) {
			return decodeJob(input);
		}
		
	};
	
	private static final Function<PrintJob, NBTTagCompound> JOB_ENCODER = new Function<PrintJob, NBTTagCompound>() {

		@Override
		public NBTTagCompound apply(PrintJob input) {
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
			jobs.add(Preconditions.checkNotNull(job));
			return true;
		}
		return false;
	}

	private Collection<PrintJob> immutableQueue;
	
	@Override
	public Collection<PrintJob> getQueue() {
		return immutableQueue == null ? (immutableQueue = Collections.unmodifiableCollection(jobs)) : immutableQueue;
	}

	@Override
	public PrintJob getCurrentJob() {
		return currentJob;
	}

	@Override
	public boolean acceptsJobs() {
		return jobs.size() < 20;
	}

}
