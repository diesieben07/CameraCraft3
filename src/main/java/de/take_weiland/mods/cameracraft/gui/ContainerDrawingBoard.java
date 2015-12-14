package de.take_weiland.mods.cameracraft.gui;

import de.take_weiland.mods.cameracraft.inv.InventoryDrawingBoard;
import de.take_weiland.mods.commons.inv.AbstractContainer;
import de.take_weiland.mods.commons.inv.Inventories;
import de.take_weiland.mods.commons.inv.SimpleSlot;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author Intektor
 */
public class ContainerDrawingBoard extends AbstractContainer<InventoryDrawingBoard> {

    private int x, y, z;

    public ContainerDrawingBoard(InventoryDrawingBoard inventory, int x, int y, int z, EntityPlayer player) {
        super(inventory, player);
        this.x = x;
        this.y = y;
        this.z = z;
    }


    @Override
    protected void addSlots() {
        addSlotToContainer(new SimpleSlot(inventory, 0, 80, 35));
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
        Inventories.spill(player.worldObj, x, y, z, inventory);
    }
}
