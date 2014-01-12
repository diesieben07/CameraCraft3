package de.take_weiland.mods.cameracraft.item;

import net.minecraft.item.Item;
import de.take_weiland.mods.commons.templates.Metadata.ItemMeta;

public enum MiscItemType implements ItemMeta {

	TIN_INGOT("ingotTin"),
	YELLOW_INK("inkYellow"),
	CYAN_INK("inkCyan"),
	MAGENTA_INK("inkMagenta"),
	BLACK_INK("inkBlack"),
	PHOTONIC_DUST("photonicDust"),
	PHOTOELECTRIC_DUST("photoelectricDust"),
	ALKALINE_DUST("alkalineDust"),
	ALKALINE_BUCKET("alkalineBucket");
	
	public static final MiscItemType[] VALUES = values();
	public static final MiscItemType[] ALL_INKS = { YELLOW_INK, CYAN_INK, MAGENTA_INK, BLACK_INK };

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
