package de.take_weiland.mods.cameracraft.item;

import de.take_weiland.mods.commons.templates.Type;

public enum CameraType implements Type {

	FILM("film"),
	DIGITAL("digital");

	private final String name;
	
	private CameraType(String name) {
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
