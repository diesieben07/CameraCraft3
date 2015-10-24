package de.take_weiland.mods.cameracraft.gui;

import de.take_weiland.mods.cameracraft.tileentity.TilePhotoProcessor;
import de.take_weiland.mods.commons.inv.AbstractContainer;
import de.take_weiland.mods.commons.inv.ShiftClickTarget;
import de.take_weiland.mods.commons.inv.SimpleSlot;
import de.take_weiland.mods.commons.inv.SpecialShiftClick;
import de.take_weiland.mods.commons.sync.Sync;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;

public class ContainerPhotoProcessor extends AbstractContainer<TilePhotoProcessor> implements SpecialShiftClick {

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
	public boolean canInteractWith(@Nonnull EntityPlayer player) {
		return inventory.isUseableByPlayer(player);
	}

    @Override
    public ShiftClickTarget getShiftClickTarget(@Nonnull ItemStack stack, @Nonnull EntityPlayer player) {
        if (FluidContainerRegistry.isFilledContainer(stack)) {
            return ShiftClickTarget.of(0);
        } else if (inventory.isValidPhotoStorage(stack)) {
            return ShiftClickTarget.of(2);
        } else {
            return ShiftClickTarget.standard();
        }
    }

	@Sync
	private FluidStack getFluid() {
		return inventory.tank.getFluid();
	}

	private void setFluid(FluidStack stack) {
		inventory.tank.setFluid(stack);
	}

	@Sync
	private int getProcessProgress() {
		return inventory.getProcessProgress();
	}

	private void setProcessProgress(int progress) {
		inventory.setProcessProgress(progress);
	}
}
