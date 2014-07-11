package de.take_weiland.mods.cameracraft.blocks;

import de.take_weiland.mods.commons.meta.Subtype;

public enum OreType implements Subtype {
	
	TIN("tin"),
	ALKALINE("alkaline"),
	BLOCK_TIN("blockTin"),
	PHOTONIC("photonic");

	private final String name;
	
	private OreType(String name) {
		this.name = name;
	}
	
	@Override
	public String subtypeName() {
		return name;
	}

}
