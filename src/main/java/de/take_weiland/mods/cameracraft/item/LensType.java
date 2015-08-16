package de.take_weiland.mods.cameracraft.item;

import de.take_weiland.mods.cameracraft.api.img.ImageFilter;
import de.take_weiland.mods.cameracraft.img.ImageFilters;
import de.take_weiland.mods.commons.meta.Subtype;

public enum LensType implements Subtype {

	RED("red", ImageFilters.RED),
	GREEN("green", ImageFilters.GREEN),
	BLUE("blue", ImageFilters.BLUE),
	YELLOW("yellow", ImageFilters.YELLOW);

	public static final LensType[] VALUES = values();
	
	private final String name;
	private final ImageFilter filter;
	
	LensType(String name, ImageFilter filter) {
		this.name = name;
		this.filter = filter;
	}

	@Override
	public String subtypeName() {
		return name;
	}

	public ImageFilter getFilter() {
		return filter;
	}

}
