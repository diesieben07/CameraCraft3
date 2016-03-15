package de.take_weiland.mods.cameracraft.item;

import de.take_weiland.mods.cameracraft.api.FilmState;
import de.take_weiland.mods.cameracraft.api.img.ImageFilter;
import de.take_weiland.mods.cameracraft.api.photo.PhotoStorage;
import de.take_weiland.mods.cameracraft.img.ImageFilters;
import de.take_weiland.mods.cameracraft.photo.PhotoStorages;
import de.take_weiland.mods.commons.meta.Subtype;
import de.take_weiland.mods.commons.util.EnumUtils;
import net.minecraft.item.ItemStack;

public enum PhotoStorageType implements Subtype {

    // FILM types must stay at the beginning and in the same order as FilmState values
    // otherwise toFilmState and getFilmState must be changed as well!
    FILM_B_W("filmBw", 24, ImageFilters.GRAY),
    FILM_B_W_EXPOSED("filmBwExposed", 24),
    FILM_B_W_READY("filmBwReady", 24),
    FILM_B_W_PROCESSED("filmBwProcessed", 24),
    FILM_COLOR("filmColor", 32),
    FILM_COLOR_EXPOSED("filmColorExposed", 32),
    FILM_COLOR_READY("filmColorReady", 32),
    FILM_COLOR_PROCESSED("filmColorProcessed", 32),
    MEMORY_CARD("memoryCard", 50);

    public static final PhotoStorageType[] VALUES = values();

    private final String      name;
    private final int         capacity;
    private final ImageFilter filter;

    PhotoStorageType(String name, int capacity) {
        this(name, capacity, null);
    }

    PhotoStorageType(String name, int capacity, ImageFilter filter) {
        this.name = name;
        this.capacity = capacity;
        this.filter = filter;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean isSealed() {
        return this == FILM_B_W_EXPOSED || this == FILM_COLOR_EXPOSED;
    }

    public PhotoStorageType getUnsealed() {
        if (isSealed()) {
            return VALUES[ordinal() - 1];
        } else {
            return this;
        }
    }

    public boolean canProcess() {
        return isSealed();
    }

    public PhotoStorageType getProcessed() {
        return canProcess() ? VALUES[ordinal() + 1] : this;
    }

    public PhotoStorage getStorage(ItemStack stack) {
        return PhotoStorages.withCapacity(capacity, isSealed(), stack, filter, this == MEMORY_CARD);
    }

    public boolean canRewind() {
        return this != MEMORY_CARD;
    }

    public PhotoStorageType rewind() {
        if (canRewind()) {
            return VALUES[ordinal() + 1];
        } else {
            return this;
        }
    }

    public boolean isScannable() {
        return this == FILM_B_W_READY || this == FILM_COLOR_PROCESSED;
    }

    @Override
    public String subtypeName() {
        return name;
    }

    public FilmState getFilmState() {
        int type = ordinal() & 0b11; // mod 4
        return EnumUtils.byOrdinal(FilmState.class, type); // this works because our order is the same as in FilmState
    }

    public ImageFilter getFilter() {
        return filter;
    }

    public PhotoStorageType applyFilmState(FilmState state) {
        int base = ordinal() & ~0b11; // round ordinal down to nearest power of 4 (0 or 4) which are the NEW film versions
        return VALUES[base + state.ordinal()]; // our values are same order as FilmState
    }
}
