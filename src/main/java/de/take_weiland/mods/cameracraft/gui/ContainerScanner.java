package de.take_weiland.mods.cameracraft.gui;

import de.take_weiland.mods.cameracraft.tileentity.TileScanner;
import de.take_weiland.mods.commons.inv.AbstractContainer;
import de.take_weiland.mods.commons.inv.SimpleSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ContainerScanner extends AbstractContainer<TileScanner> {

	protected ContainerScanner(World world, int x, int y, int z, EntityPlayer player) {
		super(world, x, y, z, player);
	}

	@Override
	protected void addSlots() {
		addSlotToContainer(new SimpleSlot(inventory, 0, 80, 35));
	}

}
