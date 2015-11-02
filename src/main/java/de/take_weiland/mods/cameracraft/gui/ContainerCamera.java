package de.take_weiland.mods.cameracraft.gui;

import cpw.mods.fml.relauncher.Side;
import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.cameracraft.api.camera.LensItem;
import de.take_weiland.mods.cameracraft.api.photo.PhotoStorageItem;
import de.take_weiland.mods.cameracraft.inv.InventoryCameraImpl;
import de.take_weiland.mods.commons.inv.*;
import de.take_weiland.mods.commons.sync.Sync;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ContainerCamera extends AbstractContainer<InventoryCameraImpl> implements ButtonContainer, SpecialShiftClick {

    public static final int BUTTON_TOGGLE_LID  = 0;
    public static final int BUTTON_REWIND_FILM = 1;

    private final int closedSlot;

    public ContainerCamera(InventoryCameraImpl upper, EntityPlayer player, int closedSlot) {
        super(upper, player);
        this.closedSlot = closedSlot;
    }

    @Sync
    private boolean getLidState() {
        return inventory.isLidClosed();
    }

    private void setLidState(boolean closed) {
        inventory.setLidState(closed);
    }

    @Override
    protected void addSlots() {
        int slots = inventory.getSizeInventory();
        int slotX = 171 - slots * 25;

        for (int slot = 0; slot < slots; ++slot) {
            addSlotToContainer(new SimpleSlot(inventory, slot, slotX, 31) {

                @Override
                public boolean canBeHovered() {
                    return isUsable() && super.canBeHovered();
                }

                @Override
                public boolean isItemValid(@Nonnull ItemStack stack) {
                    return isUsable() && super.isItemValid(stack);
                }

                @Override
                public boolean canTakeStack(EntityPlayer player) {
                    return isUsable() && super.canTakeStack(player);
                }

                private boolean isUsable() {
                    return closedSlot != getSlotIndex() || !ContainerCamera.this.inventory.isLidClosed();
                }

            });
            slotX += 25;
        }
    }

    @Override
    public void onButtonClick(Side side, EntityPlayer player, int buttonId) {
        switch (buttonId) {
            case BUTTON_TOGGLE_LID:
                inventory.toggleLid();
                break;
            case BUTTON_REWIND_FILM:
                if (side.isServer()) {
                    if (inventory.canRewind()) {
                        inventory.rewind();
                    }
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
