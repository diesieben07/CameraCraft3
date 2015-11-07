package de.take_weiland.mods.cameracraft.item;

import de.take_weiland.mods.cameracraft.api.img.ImageFilter;
import de.take_weiland.mods.cameracraft.api.photo.PhotoStorage;
import de.take_weiland.mods.cameracraft.img.ImageFilters;
import de.take_weiland.mods.cameracraft.photo.PhotoStorages;
import de.take_weiland.mods.commons.meta.Subtype;
import net.minecraft.item.ItemStack;

public enum PhotoStorageType implements Subtype {

	FILM_B_W("filmBw", 24, ImageFilters.GRAY),
	FILM_B_W_SEALED("filmBwSealed", 24),
	FILM_B_W_PROCESSED("filmBwProcessed", 24),
	FILM_COLOR("filmColor", 32),
	FILM_COLOR_SEALED("filmColorSealed", 32),
	FILM_COLOR_PROCESSED("filmColorProcessed", 32),
	MEMORY_CARD("memoryCard", 50);
	
	public static final PhotoStorageType[] VALUES = values();

	private final String name;
	private final int capacity;
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
		return this == FILM_B_W_SEALED || this == FILM_COLOR_SEALED;
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
		return PhotoStorages.withCapacity(capacity, isSealed(), stack, filter);
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
		return this == FILM_B_W_PROCESSED || this == FILM_COLOR_PROCESSED;
	}

	@Override
	public String subtypeName() {
		return name;
	}
}
