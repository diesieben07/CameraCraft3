package de.take_weiland.mods.cameracraft.gui;

import de.take_weiland.mods.cameracraft.tileentity.TilePhotoProcessor;
import de.take_weiland.mods.commons.inv.AbstractContainer;
import de.take_weiland.mods.commons.inv.SimpleSlot;
import de.take_weiland.mods.commons.sync.Synced;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

public class ContainerPhotoProcessor extends AbstractContainer<TilePhotoProcessor> {

	public ContainerPhotoProcessor(World world, int x, int y, int z, EntityPlayer player) {
		super(world, x, y, z, player);
	}
	
	@Override
	protected void addSlots() {
		addSlotToContainer(new SimpleSlot(inventory, 0, 128, 14));
		addSlotToContainer(new SimpleSlot(inventory, 1, 128, 49));
		addSlotToContainer(new SimpleSlot(inventory, 2, 100, 14));
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return inventory.isUseableByPlayer(player);
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

	@Synced(setter = "tank")
	private FluidStack getFluid() {
		return inventory.tank.getFluid();
	}

	@Synced.Setter("tank")
	private void setFluid(FluidStack stack) {
		inventory.tank.setFluid(stack);
	}

	@Synced(setter = "progress")
	private int getProcessProgress() {
		return inventory.getProcessProgress();
	}

	@Synced.Setter("progress")
	private void setProcessProgress(int progress) {
		inventory.setProcessProgress(progress);
	}
}
