package de.take_weiland.mods.cameracraft.item;

import net.minecraft.item.Item;
import de.take_weiland.mods.commons.templates.Metadata.ItemMeta;

public enum MiscItemType implements ItemMeta {

	TIN_INGOT("ingotTin"),
	CYAN_INK("inkCyan"),
	YELLOW_INK("inkYellow"),
	MAGENTA_INK("inkMagenta"),
	BLACK_INK("inkBlack"),
	PHOTONIC_DUST("photonicDust"),
	PHOTOELECTRIC_DUST("photoelectrictDust");
	
	public static final MiscItemType[] VALUES = values();
	public static final MiscItemType[] ALL_INKS = { CYAN_INK, YELLOW_INK, MAGENTA_INK, BLACK_INK };

	private final String name;
	
	private MiscItemType(String name) {
		this.name = name;
	}
	
	@Override
	public String unlocalizedName() {
		return name;
	}

	@Override
	public Item getItem() {
		return CCItem.miscItems;
	}
	
}
