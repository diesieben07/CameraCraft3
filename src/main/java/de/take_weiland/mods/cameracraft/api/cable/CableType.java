package de.take_weiland.mods.cameracraft.api.cable;

import de.take_weiland.mods.cameracraft.api.BlockType;

public interface CableType extends BlockType<CableType> {

	boolean isData();
	
	boolean isPower();
	
}
