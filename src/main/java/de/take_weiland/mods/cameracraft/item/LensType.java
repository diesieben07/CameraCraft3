package de.take_weiland.mods.cameracraft.item;

import net.minecraft.item.Item;
import de.take_weiland.mods.cameracraft.api.img.ImageFilter;
import de.take_weiland.mods.cameracraft.img.ImageFilters;
import de.take_weiland.mods.commons.templates.Metadata.ItemMeta;

public enum LensType implements ItemMeta {

	RED("red", ImageFilters.RED),
	GREEN("green", ImageFilters.GREEN),
	BLUE("blue", ImageFilters.BLUE),
	YELLOW("yellow", ImageFilters.YELLOW);

	public static final LensType[] VALUES = values();
	
	private final String name;
	private final ImageFilter filter;
	
	private LensType(String name, ImageFilter filter) {
		this.name = name;
		this.filter = filter;
	}
	
	public ImageFilter getFilter() {
		return filter;
	}

	@Override
	public String unlocalizedName() {
		return name;
	}

	@Override
	public Item getItem() {
		return CCItem.lenses;
	}

}
