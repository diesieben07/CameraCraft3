package de.take_weiland.mods.cameracraft.gui;

import cpw.mods.fml.relauncher.Side;
import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.cameracraft.api.camera.LensItem;
import de.take_weiland.mods.cameracraft.api.photo.PhotoStorageItem;
import de.take_weiland.mods.cameracraft.inv.InventoryCameraImpl;
import de.take_weiland.mods.commons.inv.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ContainerCamera extends AbstractContainer<InventoryCameraImpl> implements ButtonContainer, SpecialShiftClick {

	public static final int BUTTON_TOGGLE_LID = 0;
	public static final int BUTTON_REWIND_FILM = 1;
	
	private final int closedSlot;
	
	public ContainerCamera(InventoryCameraImpl upper, EntityPlayer player, int closedSlot) {
		super(upper, player);
		this.closedSlot = closedSlot;
	}
	
	@Override
	protected void addSlots() {
		int slots = inventory.getSizeInventory();
		int slotX = 171 - slots * 25;
		
		for (int slot = 0; slot < slots; ++slot) {
			if (slot == closedSlot) {
				addSlotToContainer(new SimpleSlot(inventory, slot, slotX, 31) {

					@Override
					public boolean canBeHovered() {
						return isUsable();
					}

					@Override
					public boolean isItemValid(@Nonnull ItemStack item) {
						return isUsable(); 
					}

					@Override
					public boolean canTakeStack(EntityPlayer player) {
						return isUsable();
					}
					
					private boolean isUsable() {
						return !ContainerCamera.this.inventory.isLidClosed();
					}
					
				});
			} else {
				addSlotToContainer(new SimpleSlot(inventory, slot, slotX, 31));
			}
			slotX += 25;
		}
	}

	@Override
	public void onButtonClick(@Nonnull Side side, @Nonnull EntityPlayer player, int buttonId) {
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
    public ShiftClickTarget getShiftClickTarget(ItemStack stack, EntityPlayer player) {
        if (stack.getItem() instanceof LensItem) {
            return ShiftClickTarget.of(InventoryCameraImpl.LENS_SLOT);
        } else if (stack.getItem() instanceof PhotoStorageItem) {
            return ShiftClickTarget.of(inventory.storageSlot());
        } else if (CameraCraft.api.findBatteryHandler(stack).isBattery(stack)) {
            return ShiftClickTarget.of(inventory.batterySlot());
        } else {
            return ShiftClickTarget.standard();
        }
    }

}
