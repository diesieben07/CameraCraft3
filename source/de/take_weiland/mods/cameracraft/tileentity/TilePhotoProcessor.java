package de.take_weiland.mods.cameracraft.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import de.take_weiland.mods.cameracraft.blocks.MachineType;
import de.take_weiland.mods.commons.templates.NameableTileEntity;
import de.take_weiland.mods.commons.templates.TileEntityInventory;
import de.take_weiland.mods.commons.util.ItemStacks;
import de.take_weiland.mods.commons.util.Multitypes;
import de.take_weiland.mods.commons.util.Sides;

public class TilePhotoProcessor extends TileEntityInventory<TilePhotoProcessor> implements IFluidHandler, NameableTileEntity {

	private static final int TANK_CAPACITY = FluidContainerRegistry.BUCKET_VOLUME * 4;
	
	private static final int CHECKING_SCHEDULED = -1;
	private static final int INVALID_ITEM = -2;
	
	private static final int FILL_DELAY = 10;
	
	private int fillCountdown = CHECKING_SCHEDULED;
	
	public final FluidTank tank = new FluidTank(TANK_CAPACITY);
	
	@Override
	public int getSizeInventory() {
		return 2;
	}

	@Override
	protected String getDefaultName() {
		return Multitypes.name(MachineType.PHOTO_PROCESSOR);
	}

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
		}
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
		return slot == 0 && FluidContainerRegistry.isFilledContainer(item);
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

}
