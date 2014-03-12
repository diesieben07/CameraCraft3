package de.take_weiland.mods.cameracraft.tileentity;

import de.take_weiland.mods.cameracraft.api.photo.PhotoStorageItem;
import de.take_weiland.mods.cameracraft.blocks.CCBlock;
import de.take_weiland.mods.cameracraft.blocks.MachineType;
import de.take_weiland.mods.commons.tileentity.TileEntityInventory;
import de.take_weiland.mods.commons.util.ItemStacks;
import de.take_weiland.mods.commons.util.Multitypes;
import de.take_weiland.mods.commons.util.Sides;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.*;

public class TilePhotoProcessor extends TileEntityInventory<TilePhotoProcessor> implements IFluidHandler {

	private static final int FLUID_PER_PROCESS = 10;

	private static final int TANK_CAPACITY = FluidContainerRegistry.BUCKET_VOLUME * 4;
	
	private static final int CHECKING_SCHEDULED = -1;
	private static final int INVALID_ITEM = -2;
	
	private static final int FILL_DELAY = 10;
	
	private int fillCountdown = CHECKING_SCHEDULED;
	private int processProgress;
	
	public final FluidTank tank = new FluidTank(TANK_CAPACITY);
	
	@Override
	public int getSizeInventory() {
		return 3;
	}

	@Override
	protected String unlocalizedName() {
		return Multitypes.name(MachineType.PHOTO_PROCESSOR);
	}

	// worldObj.getBlockLightValue(x, y, z));
	
	@Override
	public void updateEntity() {
		if (Sides.logical(this).isServer()) {
			if (fillCountdown > 0) {
				fillCountdown--;
			} else if (fillCountdown == 0) {
				processFluidInput();
			} else if (fillCountdown == CHECKING_SCHEDULED && storage[0] != null) {
				fillCountdown = FILL_DELAY;
			}
			
			boolean canProcess = canProcess();
			if (canProcess) {
				if (processProgress > 0) {
					processProgress--;
				} else if (processProgress == 0) {
					finishProcessing();
				} else {
					processProgress = 127; // TODO
				}
			} else {
				processProgress = -1;
			}
		}
	}
	
	private void finishProcessing() {
		processProgress = -1;
		storage[2] = ((PhotoStorageItem) storage[2].getItem()).process(storage[2]);
	}

	private boolean canProcess() {
		if (!isValidPhotoStorage(storage[2])) {
			return false;
		}
		FluidStack f = tank.getFluid();
		return f != null && f.fluidID == CCBlock.alkalineFluid.getID() && f.amount > FLUID_PER_PROCESS;
	}

	private void processFluidInput() {
		if (storage[0] == null) {
			fillCountdown = INVALID_ITEM;
			return;
		}
		
		ItemStack origContainer = storage[0].copy();
		ItemStack container = storage[0].splitStack(1);
		ItemStack emptied = container.getItem().getContainerItemStack(container);
		FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(container);
		
		if (ItemStacks.canMergeFully(emptied, storage[1]) && FluidContainerRegistry.isFilledContainer(container) && tank.fill(fluid, false) == fluid.amount) {
			storage[0] = ItemStacks.emptyToNull(storage[0]);
			storage[1] = ItemStacks.merge(emptied, storage[1], true);
			
			tank.fill(fluid, true);
			
			fillCountdown = CHECKING_SCHEDULED;
		} else {
			storage[0] = origContainer;
			fillCountdown = INVALID_ITEM;
		}
	}

	@Override
	public void onChange() {
		fillCountdown = CHECKING_SCHEDULED;
		super.onChange();
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack item) {
		switch (slot) {
		case 0:
			return FluidContainerRegistry.isFilledContainer(item);
		case 2:
			return isValidPhotoStorage(item);
		default:
			return false;
		}
	}

	public boolean isValidPhotoStorage(ItemStack stack) {
		Item item;
		return stack != null && (item = stack.getItem()) instanceof PhotoStorageItem && ((PhotoStorageItem) item).canBeProcessed(stack);
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		return tank.fill(resource, doFill);
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		if (resource == null || !resource.isFluidEqual(tank.getFluid())) {
			return null;
		}
		return tank.drain(resource.amount, doDrain);
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return tank.drain(maxDrain, doDrain);
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return true;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return true;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		return new FluidTankInfo[] { tank.getInfo() };
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		tank.readFromNBT(nbt.getCompoundTag("tank"));
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setCompoundTag("tank", tank.writeToNBT(new NBTTagCompound()));
	}

	public int getProcessProgress() {
		return processProgress;
	}

	public void setProcessProgress(int processProgress) {
		this.processProgress = processProgress;
	}
	
	public boolean isProcessing() {
		return processProgress >= 0;
	}

}
