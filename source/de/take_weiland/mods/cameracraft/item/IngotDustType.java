package de.take_weiland.mods.cameracraft.item;

import de.take_weiland.mods.commons.templates.Type;

public enum IngotDustType implements Type {

	TIN_INGOT("ingotTin");

	private final String name;
	
	private IngotDustType(String name) {
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
