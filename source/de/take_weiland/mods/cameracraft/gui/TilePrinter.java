package de.take_weiland.mods.cameracraft.gui;

import de.take_weiland.mods.cameracraft.blocks.MachineType;
import de.take_weiland.mods.commons.templates.TileEntityInventory;
import de.take_weiland.mods.commons.util.Multitypes;

public class TilePrinter extends TileEntityInventory<TilePrinter> {

	@Override
	public int getSizeInventory() {
		return 9;
	}

	@Override
	protected String getDefaultName() {
		return Multitypes.fullName(MachineType.PRINTER);
	}

}
