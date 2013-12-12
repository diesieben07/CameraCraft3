package de.take_weiland.mods.cameracraft.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.cameracraft.api.camera.LensItem;
import de.take_weiland.mods.cameracraft.api.photo.PhotoStorageItem;
import de.take_weiland.mods.cameracraft.inv.InventoryCamera;
import de.take_weiland.mods.commons.templates.AbstractContainer;
import de.take_weiland.mods.commons.templates.AdvancedSlot;

public class ContainerCamera extends AbstractContainer<InventoryCamera> {

	public static final int BUTTON_TOGGLE_LID = 0;
	public static final int BUTTON_REWIND_FILM = 1;
	
	private final int closedSlot;
	
	public ContainerCamera(InventoryCamera upper, EntityPlayer player, int closedSlot) {
		super(upper, player);
		this.closedSlot = closedSlot;
	}
	
	@Override
	protected void addSlots() {
		int slots = inventory.getSizeInventory();
		int slotX = 171 - slots * 25;
		
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
		return buttonId == BUTTON_TOGGLE_LID || buttonId == BUTTON_REWIND_FILM;
	}

	@Override
	public void onButtonClick(Side side, EntityPlayer player, int buttonId) {
		switch (buttonId) {
		case BUTTON_TOGGLE_LID:
			inventory.toggleLid();
			break;
		case BUTTON_REWIND_FILM:
			if (inventory.canRewind()) {
				inventory.rewind();
			}
			break;
		}
	}
	
	@Override
	protected int getSlotFor(ItemStack stack) {
		if (stack.getItem() instanceof LensItem) {
			return InventoryCamera.LENS_SLOT;
		} else if (stack.getItem() instanceof PhotoStorageItem) {
			return inventory.storageSlot();
		} else if (CameraCraft.api.isBattery(stack)) {
			return inventory.batterySlot();
		}
		return -1;
	}
	
}
