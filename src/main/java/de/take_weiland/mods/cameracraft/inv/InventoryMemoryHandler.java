package de.take_weiland.mods.cameracraft.inv;

import de.take_weiland.mods.cameracraft.item.CCItem;
import de.take_weiland.mods.cameracraft.item.PhotoStorageType;
import de.take_weiland.mods.commons.inv.SimpleInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * @author Intektor
 */
public class InventoryMemoryHandler implements SimpleInventory {

    ItemStack[] slots = new ItemStack[2];

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
        return "MemoryHandler";
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

        if(slot == 0 || slot == 1) {
            if(stack != null) {
                if(CCItem.photoStorage.getType(stack) == PhotoStorageType.MEMORY_CARD) {
                    return true;
                }
            }
        }

        return false;
    }
}
