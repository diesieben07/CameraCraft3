package de.take_weiland.mods.cameracraft.item;

import net.minecraft.item.Item;
import de.take_weiland.mods.commons.templates.Metadata.ItemMeta;

public enum PhotoType implements ItemMeta {
	;

	public static final PhotoType[] VALUES = values();
	
	private final String name;
	
	private PhotoType(String name) {
		this.name = name;
	}
	
	@Override
	public String unlocalizedName() {
		return name;
	}

	@Override
	public Item getItem() {
		return CCItem.photo;
	}

}
