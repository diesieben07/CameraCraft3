package de.take_weiland.mods.cameracraft.gui;

import de.take_weiland.mods.cameracraft.tileentity.TileDrawingBoard;
import de.take_weiland.mods.commons.inv.AbstractContainer;
import de.take_weiland.mods.commons.inv.SimpleSlot;
import de.take_weiland.mods.commons.inv.SlotNoPickup;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * @author Intektor
 */
public class ContainerDrawingBoard extends AbstractContainer<TileDrawingBoard> {

    public ContainerDrawingBoard(World world, int x, int y, int z, EntityPlayer player) {
        super(world, x, y, z, player);
    }


    @Override
    protected void addSlots() {
        addSlotToContainer(new SimpleSlot(inventory, 0, 80, 35));
        addSlotToContainer(new SlotNoPickup(inventory, 1, 0, 0));
    }


}
