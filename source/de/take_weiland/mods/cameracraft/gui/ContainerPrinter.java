package de.take_weiland.mods.cameracraft.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import de.take_weiland.mods.cameracraft.item.MiscItemType;
import de.take_weiland.mods.commons.templates.AbstractContainer;
import de.take_weiland.mods.commons.templates.AdvancedSlot;
import de.take_weiland.mods.commons.util.ItemStacks;

public class ContainerPrinter extends AbstractContainer<TilePrinter> {

	protected ContainerPrinter(World world, int x, int y, int z, EntityPlayer player) {
		super(world, x, y, z, player);
	}

	private static final int[] SLOT_BOUNDS = new int[] { 0, 9 };
	
	@Override
	public int[] getSlotsFor(ItemStack stack) {
		if (ItemStacks.isAny(stack, MiscItemType.ALL_INKS)) {
			return SLOT_BOUNDS;
		} else {
			return null;
		}
	}

	@Override
	protected void addSlots() {
		for (int y = 0; y < 3; ++y) {
			for (int x = 0; x < 3; ++x) {
				addSlotToContainer(new AdvancedSlot(inventory, 3 * x + y, x * 18 + 116, y * 18 + 17));
			}
		}
	}

}
