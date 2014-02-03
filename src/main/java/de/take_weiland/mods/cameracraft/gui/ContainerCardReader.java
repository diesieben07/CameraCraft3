package de.take_weiland.mods.cameracraft.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import de.take_weiland.mods.cameracraft.item.PhotoStorageType;
import de.take_weiland.mods.cameracraft.tileentity.TileCardReader;
import de.take_weiland.mods.commons.sync.Synced;
import de.take_weiland.mods.commons.templates.AbstractContainer;
import de.take_weiland.mods.commons.templates.AdvancedSlot;
import de.take_weiland.mods.commons.util.ItemStacks;

@Synced
public class ContainerCardReader extends AbstractContainer<TileCardReader> {

	@de.take_weiland.mods.commons.sync.Synced
	private byte accessState;
	
	public ContainerCardReader(World world, int x, int y, int z, EntityPlayer player) {
		super(world, x, y, z, player);
	}

	@Override
	public int getSlotFor(ItemStack stack) {
		return ItemStacks.is(stack, PhotoStorageType.MEMORY_CARD) ? 0 : -1;
	}

	@Override
	protected void addSlots() {
		addSlotToContainer(new AdvancedSlot(inventory, 0, 80, 35));
	}
	
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		accessState = (byte) inventory.getAccessState();
	}

	public int getAccessState() {
		return accessState;
	}

}
