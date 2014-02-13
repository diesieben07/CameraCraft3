package de.take_weiland.mods.cameracraft.gui;

import de.take_weiland.mods.cameracraft.tileentity.TilePhotoProcessor;
import de.take_weiland.mods.commons.sync.Synced;
import de.take_weiland.mods.commons.templates.AbstractContainer;
import de.take_weiland.mods.commons.templates.AdvancedSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

@Synced
public class ContainerPhotoProcessor extends AbstractContainer<TilePhotoProcessor> {

	public ContainerPhotoProcessor(World world, int x, int y, int z, EntityPlayer player) {
		super(world, x, y, z, player);
	}
	
	@Override
	protected void addSlots() {
		addSlotToContainer(new AdvancedSlot(inventory, 0, 128, 14));
		addSlotToContainer(new AdvancedSlot(inventory, 1, 128, 49));
		addSlotToContainer(new AdvancedSlot(inventory, 2, 100, 14));
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return inventory.isUseableByPlayer(player);
	}

	@de.take_weiland.mods.commons.sync.Synced(setter = "tank")
	private FluidStack getFluid() {
		return inventory.tank.getFluid();
	}

	@de.take_weiland.mods.commons.sync.Synced.Setter("tank")
	private void setFluid(FluidStack stack) {
		inventory.tank.setFluid(stack);
	}

	@de.take_weiland.mods.commons.sync.Synced(setter = "progress")
	private int getProcessProgress() {
		return inventory.getProcessProgress();
	}

	@de.take_weiland.mods.commons.sync.Synced.Setter("progress")
	private void setProcessProgress(int progress) {
		inventory.setProcessProgress(progress);
	}
	
	@Override
	public int getSlotFor(ItemStack stack) {
		if (FluidContainerRegistry.isFilledContainer(stack)) {
			return 0;
		} else if (inventory.isValidPhotoStorage(stack)) {
			return 2;
		} else {
			return -1;
		}
	}

	@Override
	public int getFirstPlayerSlot() {
		return inventory.getSizeInventory();
	}

}
