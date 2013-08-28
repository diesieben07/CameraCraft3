package de.take_weiland.mods.cameracraft.gui;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import de.take_weiland.mods.cameracraft.tileentity.TilePhotoProcessor;
import de.take_weiland.mods.commons.gui.AbstractContainer;
import de.take_weiland.mods.commons.gui.AdvancedSlot;

public class ContainerPhotoProcessor extends AbstractContainer<TilePhotoProcessor> {

	private final int FLUID_ID = 0;
	private final int FLUID_AMOUNT = 1;
	
	public int lastFluidId;
	public int lastFluidAmount;
	
	public ContainerPhotoProcessor(World world, int x, int y, int z, EntityPlayer player) {
		super(world, x, y, z, player);
	}
	
	@Override
	protected void addSlots() {
		addSlotToContainer(new AdvancedSlot(inventory, 0, 128, 14));
		addSlotToContainer(new AdvancedSlot(inventory, 1, 128, 49));
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return inventory.isUseableByPlayer(player);
	}
	
	@Override
	protected boolean enableSyncing() {
		return true;
	}

	@Override
	public boolean prepareSyncData() {
		FluidStack fluid = inventory.tank.getFluid();
		int newFluidId = fluid == null ? 0 : fluid.fluidID;
		int newFluidAmount = fluid == null ? 0 : fluid.amount;
		boolean needsSync = newFluidId != lastFluidId || newFluidAmount != lastFluidAmount;
		lastFluidAmount = newFluidAmount;
		lastFluidId = newFluidId;
		return needsSync;
	}

	@Override
	public void writeSyncData(ByteArrayDataOutput out, boolean all) {
//		if (all)
	}

	@Override
	public void readSyncData(ByteArrayDataInput in) {
		// TODO Auto-generated method stub
		super.readSyncData(in);
	}

	@Override
	public void addCraftingToCrafters(ICrafting crafter) {
		super.addCraftingToCrafters(crafter);
		FluidStack fluid = inventory.tank.getFluid();
		crafter.sendProgressBarUpdate(this, FLUID_ID, fluid == null ? 0 : fluid.fluidID);
		crafter.sendProgressBarUpdate(this, FLUID_AMOUNT, fluid == null ? 0 : fluid.amount);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		FluidStack fluid = inventory.tank.getFluid();
		int newFluidId = fluid == null ? 0 : fluid.fluidID;
		int newFluidAmount = fluid == null ? 0 : fluid.amount;
		for (ICrafting crafter : (List<ICrafting>)crafters) {
			if (lastFluidId != newFluidId) {
				crafter.sendProgressBarUpdate(this, FLUID_ID, newFluidId);
			}
			if (lastFluidAmount != newFluidAmount) {
				crafter.sendProgressBarUpdate(this, FLUID_AMOUNT, newFluidAmount);
			}
		}
		lastFluidId = newFluidId;
		lastFluidAmount = newFluidAmount;
	}

	@Override
	public void updateProgressBar(int id, int value) {
		FluidTank tank = inventory.tank;
		
		switch (id) {
		case FLUID_ID:
			if (value == 0) {
				tank.setFluid(null);
			} else {
				if (tank.getFluid() == null) {
					tank.setFluid(new FluidStack(value, 0));
				} else {
					tank.getFluid().fluidID = value;
				}
			}
			break;
		case FLUID_AMOUNT:
			if (tank.getFluid() == null) {
				tank.setFluid(new FluidStack(FluidRegistry.WATER, value));
			} else {
				tank.getFluid().amount = value;
			}
			break;
		}
	}

	@Override
	public int getMergeTargetSlot(ItemStack stack) {
		if (FluidContainerRegistry.isFilledContainer(stack)) {
			return 0;
		} else {
			return -1;
		}
	}

	@Override
	public int getFirstPlayerSlot() {
		return inventory.getSizeInventory();
	}
	
}
