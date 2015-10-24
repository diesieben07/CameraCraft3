package de.take_weiland.mods.cameracraft.gui;

import de.take_weiland.mods.cameracraft.tileentity.TileScanner;
import de.take_weiland.mods.commons.inv.AbstractContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * @author diesieben07
 */
public class ContainerScanner extends AbstractContainer<TileScanner> {
    public ContainerScanner(World world, int x, int y, int z, EntityPlayer player) {
        super(world, x, y, z, player);
    }

    @Override
    protected void addSlots() {

    }
}
