package de.take_weiland.mods.cameracraft.blocks;

import de.take_weiland.mods.commons.templates.Type;

public enum CableType implements Type {
	POWER("power"),
	DATA("data");

	private final String name;
	
	private CableType(String name) {
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
	
	public de.take_weiland.mods.cameracraft.api.cable.CableType toApiForm() {
		return de.take_weiland.mods.cameracraft.api.cable.CableType.values()[ordinal()];
	}
	
	public static CableType fromApi(de.take_weiland.mods.cameracraft.api.cable.CableType apiForm) {
		return values()[apiForm.ordinal()];
	}

}
