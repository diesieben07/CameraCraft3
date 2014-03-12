package de.take_weiland.mods.cameracraft.gui;

import de.take_weiland.mods.cameracraft.item.PhotoStorageType;
import de.take_weiland.mods.cameracraft.tileentity.TileCardReader;
import de.take_weiland.mods.commons.inv.AbstractContainer;
import de.take_weiland.mods.commons.inv.SimpleSlot;
import de.take_weiland.mods.commons.sync.Synced;
import de.take_weiland.mods.commons.util.ItemStacks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerCardReader extends AbstractContainer<TileCardReader> {

	public ContainerCardReader(World world, int x, int y, int z, EntityPlayer player) {
		super(world, x, y, z, player);
	}

	@Override
	public int getSlotFor(ItemStack stack) {
		return ItemStacks.is(stack, PhotoStorageType.MEMORY_CARD) ? 0 : -1;
	}

	@Override
	protected void addSlots() {
		addSlotToContainer(new SimpleSlot(inventory, 0, 80, 35));
	}
	
	@Synced(setter = "accessState")
	public int getAccessState() {
		return inventory.getAccessState();
	}

	@Synced.Setter("accessState")
	private void setAccessState(int accessState) {
		inventory.setAccessState(accessState);
	}

}
