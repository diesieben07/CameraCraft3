package de.take_weiland.mods.cameracraft.tileentity;

import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.cameracraft.api.photo.PhotoStorage;
import de.take_weiland.mods.cameracraft.api.photo.PhotoStorageItem;
import de.take_weiland.mods.cameracraft.blocks.CCBlock;
import de.take_weiland.mods.cameracraft.blocks.MachineType;
import de.take_weiland.mods.cameracraft.img.ImageFilters;
import de.take_weiland.mods.commons.nbt.ToNbt;
import de.take_weiland.mods.commons.sync.Sync;
import de.take_weiland.mods.commons.tileentity.TileEntityInventory;
import de.take_weiland.mods.commons.util.Fluids;
import de.take_weiland.mods.commons.util.ItemStacks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

import static de.take_weiland.mods.commons.util.Sides.sideOf;

public class TilePhotoProcessor extends TileEntityInventory implements IFluidHandler {

	private static final int FLUID_PER_PROCESS = 10;

	private static final int TANK_CAPACITY = FluidContainerRegistry.BUCKET_VOLUME * 4;
	
	private static final int CHECKING_SCHEDULED = -1;
	private static final int INVALID_ITEM = -2;
	
	private static final int FILL_DELAY = 10;

    private static final int MAX_LIGHT_LEVEL = 2;
	
	private int fillCountdown = CHECKING_SCHEDULED;
	private int processProgress;

    @ToNbt
	@Sync
	public final FluidTank tank = new FluidTank(TANK_CAPACITY);

    private FluidStack oldTankFluid;

	@Override
	public int getSizeInventory() {
		return 3;
	}

	@Override
	public void updateEntity() {
		if (sideOf(this).isServer()) {
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
		} else {
            if (!Fluids.identical(oldTankFluid, tank.getFluid())) {
                oldTankFluid = Fluids.clone(tank.getFluid());
                worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
            }
        }
	}
	
	private void finishProcessing() {
		processProgress = -1;
        PhotoStorageItem item = (PhotoStorageItem) storage[2].getItem();
        storage[2] = item.process(storage[2]);
        if (!isInDarkness()) {
            PhotoStorage photoStorage = item.getPhotoStorage(storage[2]);
            for (long photoId : photoStorage) {
				CameraCraft.currentDatabase().applyFilter(photoId, ImageFilters.OVEREXPOSE);
            }
        }
	}

    private boolean isInDarkness() {
        return worldObj.getBlockLightValue(xCoord, yCoord, zCoord) <= MAX_LIGHT_LEVEL;
    }

	private boolean canProcess() {
		if (!isValidPhotoStorage(storage[2])) {
			return false;
		}
		FluidStack f = tank.getFluid();
		return f != null && f.getFluid() == CCBlock.alkalineFluid && f.amount > FLUID_PER_PROCESS;
	}

	private void processFluidInput() {
		if (storage[0] == null) {
			fillCountdown = INVALID_ITEM;
			return;
		}

        ItemStack container = storage[0].copy();
        container.stackSize = 1;

        ItemStack processed = null;
        Runnable performAction = null;
        if (FluidContainerRegistry.isFilledContainer(container)) {
            FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(container);
            int filled = tank.fill(fluid, false);
            if (filled == fluid.amount) {
                processed = FluidContainerRegistry.drainFluidContainer(container);
                performAction = () -> tank.fill(fluid, true);
            }
        } else if (tank.getFluidAmount() != 0 && FluidContainerRegistry.isEmptyContainer(container)) {
            FluidStack fluid = tank.getFluid();
            ItemStack filled = FluidContainerRegistry.fillFluidContainer(fluid, container);
            if (filled != null) {
                processed = filled;
                performAction = () -> tank.drain(FluidContainerRegistry.getFluidForFilledItem(filled).amount, true);
            }
        }

        if (processed != null && ItemStacks.fitsInto(processed, storage[1])) {
            storage[0].stackSize--;
            storage[0] = ItemStacks.emptyToNull(storage[0]);
            storage[1] = ItemStacks.merge(processed, storage[1], true);

            fillCountdown = CHECKING_SCHEDULED;
            performAction.run();
        } else {
            fillCountdown = INVALID_ITEM;
        }
	}

    @Override
    public void markDirty() {
        fillCountdown = CHECKING_SCHEDULED;
        super.markDirty();
    }

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		switch (slot) {
		case 0:
			return FluidContainerRegistry.isContainer(stack);
		case 2:
			return isValidPhotoStorage(stack);
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

	public int getProcessProgress() {
		return processProgress;
	}

	public void setProcessProgress(int processProgress) {
		this.processProgress = processProgress;
	}
	
	public boolean isProcessing() {
        return processProgress >= 0;
    }

	@Override
	public String getDefaultName() {
		return CCBlock.machines.get(MachineType.PHOTO_PROCESSOR).getUnlocalizedName();

	}

}
