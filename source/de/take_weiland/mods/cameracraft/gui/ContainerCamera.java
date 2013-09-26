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
		int slots = inventory.getType().getSlots();
		int slotX = 171 - slots * 25;
		
		for (int slot = 0; slot < slots; ++slot) {
			addSlotToContainer(new AdvancedSlot(inventory, slot, slotX, 31));
			slotX += 25;
		}
		
	}

	@Override
	public int getMergeTargetSlot(ItemStack stack) {
		return -1;
	}

}
