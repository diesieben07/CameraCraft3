package de.take_weiland.mods.cameracraft.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import de.take_weiland.mods.cameracraft.tileentity.TilePhotoProcessor;
import de.take_weiland.mods.commons.gui.AbstractContainer;
import de.take_weiland.mods.commons.gui.AdvancedSlot;
import de.take_weiland.mods.commons.syncing.Synced;

public class ContainerPhotoProcessor extends AbstractContainer<TilePhotoProcessor> implements Synced {

	@Sync
	private FluidStack fluid;
	
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
	public void downloadSyncedFields() {
		FluidStack fluid = inventory.tank.getFluid();
		this.fluid = fluid == null ? null : fluid.copy();
	}
	
	@Override
	public void uploadSyncedFields() {
		inventory.tank.setFluid(fluid);
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
