package de.take_weiland.mods.cameracraft.gui;

import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import de.take_weiland.mods.cameracraft.inv.InventoryCamera;
import de.take_weiland.mods.commons.templates.AbstractContainer;
import de.take_weiland.mods.commons.templates.AdvancedSlot;

public class ContainerCamera extends AbstractContainer<InventoryCamera> {

	public static final int BUTTON_TOGGLE_LID = 0;
	
	protected ContainerCamera(InventoryCamera upper, EntityPlayer player) {
		super(upper, player);
	}
	
	@Override
	protected void addSlots() {
		int slots = inventory.getType().getSlots();
		int slotX = 171 - slots * 25;
		
		int closedSlot = inventory.storageSlot();
		for (int slot = 0; slot < slots; ++slot) {
			if (slot == closedSlot) {
				addSlotToContainer(new AdvancedSlot(inventory, slot, slotX, 31) {

					@Override
					public boolean func_111238_b() {
						return isUsable();
					}

					@Override
					public boolean isItemValid(ItemStack item) {
						return isUsable(); 
					}

					@Override
					public boolean canTakeStack(EntityPlayer player) {
						return isUsable();
					}
					
					private boolean isUsable() {
						return !ContainerCamera.this.inventory().isLidClosed();
					}
					
				});
			} else {
				addSlotToContainer(new AdvancedSlot(inventory, slot, slotX, 31));
			}
			slotX += 25;
		}
	}

	@Override
	public boolean handlesButton(EntityPlayer player, int buttonId) {
		return buttonId == BUTTON_TOGGLE_LID;
	}

	@Override
	public void clickButton(Side side, EntityPlayer player, int buttonId) {
		inventory.toggleLid();
	}
	
	@Override
	public int getMergeTargetSlot(ItemStack stack) {
		return -1;
	}
	
}
