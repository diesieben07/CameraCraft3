package de.take_weiland.mods.cameracraft.gui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import de.take_weiland.mods.cameracraft.tileentity.TilePhotoProcessor;
import de.take_weiland.mods.commons.network.Packets;
import de.take_weiland.mods.commons.templates.AbstractContainer;
import de.take_weiland.mods.commons.templates.AdvancedSlot;

public class ContainerPhotoProcessor extends AbstractContainer.Synced<TilePhotoProcessor> {

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
	public boolean writeSyncData(DataOutputStream out) throws IOException {
		Packets.writeFluidStack(out, inventory.tank.getFluid());
		return true;
	}

	@Override
	public void readSyncData(DataInputStream in) throws IOException {
		inventory.tank.setFluid(Packets.readFluidStack(in));
	}

	@Override
	public int getSlotFor(ItemStack stack) {
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
