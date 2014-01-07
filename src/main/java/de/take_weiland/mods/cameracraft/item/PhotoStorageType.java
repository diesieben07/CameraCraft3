package de.take_weiland.mods.cameracraft.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import de.take_weiland.mods.cameracraft.api.img.ImageFilter;
import de.take_weiland.mods.cameracraft.api.photo.PhotoStorage;
import de.take_weiland.mods.cameracraft.img.ImageFilters;
import de.take_weiland.mods.cameracraft.photo.PhotoStorages;
import de.take_weiland.mods.commons.templates.Metadata.ItemMeta;

public enum PhotoStorageType implements ItemMeta {

	FILM_B_W("filmBw", 24, false, ImageFilters.GRAY),
	FILM_B_W_SEALED("filmBwSealed", 24, true, true),
	FILM_B_W_PROCESSED("filmBwProcessed", 24, true),
	FILM_COLOR("filmColor", 32, false),
	FILM_COLOR_SEALED("filmColorSealed", 32, true, true),
	FILM_COLOR_PROCESSED("filmBwProcessed", 32, true),
	MEMORY_CARD("memoryCard", 50, false);
	
	public static final PhotoStorageType[] VALUES = values();

	private final String name;
	private final int capacity;
	private final boolean isSealed;
	private final boolean canProcess;
	private final ImageFilter filter;
	
	private PhotoStorageType(String name, int capacity, boolean isSealed) {
		this(name, capacity, isSealed, null);
	}
	
	private PhotoStorageType(String name, int capacity, boolean isSealed, boolean canProcess) {
		this(name, capacity, isSealed, canProcess, null);
	}
	
	private PhotoStorageType(String name, int capacity, boolean isSealed, ImageFilter filter) {
		this(name, capacity, isSealed, false, filter);
	}
	
	private PhotoStorageType(String name, int capacity, boolean isSealed, boolean canProcess, ImageFilter filter) {
		this.name = name;
		this.capacity = capacity;
		this.isSealed = isSealed;
		this.filter = filter;
		this.canProcess = canProcess;
	}
	
	public int getCapacity() {
		return capacity;
	}
	
	public boolean isSealed() {
		return isSealed;
	}
	
	public PhotoStorageType getUnsealed() {
		if (isSealed) {
			return VALUES[ordinal() - 1];
		} else {
			return this;
		}
	}
	
	public boolean canProcess() {
		return canProcess;
	}
	
	public PhotoStorageType getProcessed() {
		return canProcess ? VALUES[ordinal() + 1] : this;
	}
	
	public PhotoStorage getStorage(ItemStack stack) {
		return PhotoStorages.withCapacity(capacity, isSealed, stack, filter);
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

	@Override
	public String unlocalizedName() {
		return name;
	}

	@Override
	public Item getItem() {
		return CCItem.photoStorage;
	}
	
}
