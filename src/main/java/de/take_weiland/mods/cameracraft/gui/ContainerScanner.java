package de.take_weiland.mods.cameracraft.gui;

import cpw.mods.fml.relauncher.Side;
import de.take_weiland.mods.cameracraft.tileentity.TileScanner;
import de.take_weiland.mods.commons.inv.AbstractContainer;
import de.take_weiland.mods.commons.inv.ButtonContainer;
import de.take_weiland.mods.commons.inv.SimpleSlot;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @author diesieben07
 */
@ParametersAreNonnullByDefault
public class ContainerScanner extends AbstractContainer<TileScanner> implements ButtonContainer {

    public ContainerScanner(EntityPlayer player, TileScanner inventory) {
        super(player, inventory);
    }

    @Override
    protected void addSlots() {
        addSlotToContainer(new SimpleSlot(inventory, 0, 59, 35));
        addSlotToContainer(new SimpleSlot(inventory, 1, 101, 35));
    }

    @Override
    public void onButtonClick(Side side, EntityPlayer player, int buttonId) {
        inventory.requestScan();
    }
}
