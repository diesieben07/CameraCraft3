package de.take_weiland.mods.cameracraft.api.cable;

import de.take_weiland.mods.cameracraft.api.ItemType;

public interface CableType extends ItemType<CableType> {

	boolean isData();
	
	boolean isPower();
	
}
