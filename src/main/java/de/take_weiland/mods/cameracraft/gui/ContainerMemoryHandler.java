package de.take_weiland.mods.cameracraft.gui;

import cpw.mods.fml.relauncher.Side;
import de.take_weiland.mods.cameracraft.api.photo.PhotoStorage;
import de.take_weiland.mods.cameracraft.inv.InventoryMemoryHandler;
import de.take_weiland.mods.cameracraft.item.ItemPhotoStorages;
import de.take_weiland.mods.commons.inv.AbstractContainer;
import de.take_weiland.mods.commons.inv.ButtonContainer;
import de.take_weiland.mods.commons.inv.Inventories;
import de.take_weiland.mods.commons.inv.SimpleSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * @author Intektor
 */
public class ContainerMemoryHandler extends AbstractContainer<InventoryMemoryHandler> implements ButtonContainer {

    private int x, y, z;

    public ContainerMemoryHandler(InventoryMemoryHandler inventory, int x, int y, int z, EntityPlayer player) {
        super(inventory, player);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    protected void addSlots() {
        addSlotToContainer(new SimpleSlot(inventory, 0, 8, 62));
        addSlotToContainer(new SimpleSlot(inventory, 1, 151, 62));
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
        Inventories.spill(player.worldObj, x, y, z, inventory);
    }

    @Override
    public void onButtonClick(Side side, EntityPlayer player, int buttonId) {
        int stateID = buttonId & 0xFFFF_FFFF;
        int buttonID = buttonId >>> 16;

        if (stateID == 65536) {
            if (buttonID == 1) {
                SimpleSlot slot = (SimpleSlot) inventorySlots.get(0);
                slot.allowPickUp(false);
                slot = (SimpleSlot) inventorySlots.get(1);
                slot.allowPickUp(false);
            }
        }

        if (stateID == 65538) {
            ItemStack stack = ((Slot) inventorySlots.get(0)).getStack();
            ItemPhotoStorages itemStorage = (ItemPhotoStorages) stack.getItem();
            PhotoStorage storage = itemStorage.getPhotoStorage(stack);
            switch (buttonID) {
                case 2:

                    break;
                case 3:

                    break;
                case 4:
                    break;
            }
        }

    }
}
