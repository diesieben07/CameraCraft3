package de.take_weiland.mods.cameracraft.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import de.take_weiland.mods.cameracraft.inv.InventoryCamera;
import de.take_weiland.mods.commons.gui.AbstractContainer;
import de.take_weiland.mods.commons.gui.AdvancedSlot;

public class ContainerCamera extends AbstractContainer<InventoryCamera> {

	protected ContainerCamera(InventoryCamera upper, EntityPlayer player) {
		super(upper, player);
	}
	
	@Override
	protected void addSlots() {
		addSlotToContainer(new AdvancedSlot(inventory, 0, 121, 31));
		addSlotToContainer(new AdvancedSlot(inventory, 1, 146, 31));
	}

	@Override
	public int getMergeTargetSlot(ItemStack stack) {
		return -1;
	}

}
