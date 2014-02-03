package de.take_weiland.mods.cameracraft.item;

import net.minecraft.item.Item;
import de.take_weiland.mods.cameracraft.api.printer.InkItem;
import de.take_weiland.mods.cameracraft.api.printer.InkItem.Color;
import de.take_weiland.mods.commons.templates.Metadata.ItemMeta;

public enum MiscItemType implements ItemMeta {

	TIN_INGOT("ingotTin"),
	YELLOW_INK("inkYellow", true),
	CYAN_INK("inkCyan", true),
	MAGENTA_INK("inkMagenta", true),
	BLACK_INK("inkBlack", true),
	PHOTONIC_DUST("photonicDust"),
	PHOTOELECTRIC_DUST("photoelectricDust"),
	ALKALINE_DUST("alkalineDust"),
	ALKALINE_BUCKET("alkalineBucket");
	
	public static final MiscItemType[] VALUES = values();
	public static final MiscItemType[] ALL_INKS = { YELLOW_INK, CYAN_INK, MAGENTA_INK, BLACK_INK };

	private final boolean isInk;
	private final String name;
	
	private MiscItemType(String name) {
		this(name, false);
	}
	
	private MiscItemType(String name, boolean isInk) {
		this.name = name;
		this.isInk = isInk;
	}
	
	@Override
	public String unlocalizedName() {
		return name;
	}

	@Override
	public Item getItem() {
		return CCItem.miscItems;
	}
	
	public boolean isInk() {
		return isInk;
	}

	public InkItem.Color getInkColor() {
		if (isInk) {
			switch (this) {
			case CYAN_INK:
				return Color.CYAN;
			case YELLOW_INK:
				return Color.YELLOW;
			case MAGENTA_INK:
				return Color.MAGENTA;
			case BLACK_INK:
			default: // doesn't happen
				return Color.BLACK;
			}
		}
		return null;
	}
	
}
