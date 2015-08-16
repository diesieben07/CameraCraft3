package de.take_weiland.mods.cameracraft.item;

import de.take_weiland.mods.commons.meta.Subtype;

public enum PhotoType implements Subtype {
	PHOTO("photo"),
	POSTER("poster");

	public static final PhotoType[] VALUES = values();
	
	private final String name;
	
	PhotoType(String name) {
		this.name = name;
	}

	@Override
	public String subtypeName() {
		return name;
	}
}
