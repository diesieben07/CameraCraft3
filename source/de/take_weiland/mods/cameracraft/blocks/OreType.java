package de.take_weiland.mods.cameracraft.blocks;

import de.take_weiland.mods.commons.templates.Type;

public enum OreType implements Type {
	TIN("tin"),
	ALKALINE("alkaline");

	private final String name;
	
	private OreType(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getMeta() {
		return ordinal();
	}

}
