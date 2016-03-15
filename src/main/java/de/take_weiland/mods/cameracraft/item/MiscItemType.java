package de.take_weiland.mods.cameracraft.item;

import de.take_weiland.mods.cameracraft.api.printer.InkItem;
import de.take_weiland.mods.cameracraft.api.printer.InkItem.Color;
import de.take_weiland.mods.commons.meta.Subtype;
import net.minecraft.item.ItemStack;

public enum MiscItemType implements Subtype {

    TIN_INGOT("ingotTin"),
    YELLOW_INK("inkYellow", true),
    CYAN_INK("inkCyan", true),
    MAGENTA_INK("inkMagenta", true),
    BLACK_INK("inkBlack", true),
    PHOTONIC_DUST("photonicDust"),
    PHOTOELECTRIC_DUST("photoelectricDust"),
    SULFUR_DUST("sulfurDust"),
    PHOTO_PAPER("photoPaper"),
    DEVELOPER("developer"),
    FIXER("fixer"),
    TRAY("tray"),
    DEVELOPMENT_TANK("developmentTank"),
    DIRTY_WATER("dirty_water");

    private final boolean isInk;
    private final String  name;

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

    public int getStackSize(ItemStack stack) {
        if (isInk || this == DEVELOPER || this == FIXER || this == TRAY && CCItem.misc.getContainedChemical(stack) != null) {
            return 1;
        } else {
            return 64;
        }
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
                throw new UnsupportedOperationException();
        }
    }

    public int getDyeMeta() {
        switch (this) {
            case CYAN_INK:
                return 6;
            case YELLOW_INK:
                return 11;
            case MAGENTA_INK:
                return 13;
            case BLACK_INK:
                return 0;
            default:
                throw new UnsupportedOperationException();
        }
    }

    public boolean isChemical() {
        return this == FIXER || this == DEVELOPER || this == DIRTY_WATER;
    }

}
