package de.take_weiland.mods.cameracraft.inv;

import de.take_weiland.mods.cameracraft.item.ItemPhoto;
import de.take_weiland.mods.commons.inv.SimpleInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * @author Intektor
 */
public class InventoryDrawingBoard implements SimpleInventory {

    ItemStack[] slots = new ItemStack[1];

    @Override
    public void setSlotNoMark(int slot, ItemStack stack) {
        slots[slot] = stack;
    }

    @Override
    public int getSizeInventory() {
        return slots.length;
    }

    @Override
    public ItemStack getStackInSlot(int slotIn) {
        return slots[slotIn];
    }

    @Override
    public String getInventoryName() {
        return "Drawing_Board";
    }

    @Override
    public boolean isCustomInventoryName() {
        return false;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        if(slot == 0) {
            if(stack != null) {
                if(stack.getItem() instanceof ItemPhoto) {
                    return true;
                }
            }
        }

        return false;
    }
}
