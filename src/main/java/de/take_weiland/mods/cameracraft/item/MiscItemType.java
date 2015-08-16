package de.take_weiland.mods.cameracraft.item;

import de.take_weiland.mods.cameracraft.api.printer.InkItem;
import de.take_weiland.mods.cameracraft.api.printer.InkItem.Color;
import de.take_weiland.mods.commons.meta.Subtype;

public enum MiscItemType implements Subtype {

	TIN_INGOT("ingotTin"),
	YELLOW_INK("inkYellow", true),
	CYAN_INK("inkCyan", true),
	MAGENTA_INK("inkMagenta", true),
	BLACK_INK("inkBlack", true),
	PHOTONIC_DUST("photonicDust"),
	PHOTOELECTRIC_DUST("photoelectricDust"),
	ALKALINE_DUST("alkalineDust"),
	ALKALINE_BUCKET("alkalineBucket");
	
	private final boolean isInk;
	private final String name;
	
	MiscItemType(String name) {
		this(name, false);
	}
	
	MiscItemType(String name, boolean isInk) {
		this.name = name;
		this.isInk = isInk;
	}



	@Override
	public String subtypeName() {
		return name;
	}

	public boolean isInk() {
		return isInk;
	}

	public InkItem.Color getInkColor() {
        switch (this) {
            case CYAN_INK:
                return Color.CYAN;
            case YELLOW_INK:
                return Color.YELLOW;
            case MAGENTA_INK:
                return Color.MAGENTA;
            case BLACK_INK:
                return Color.BLACK;
            default:
                return null;
        }
	}
}
