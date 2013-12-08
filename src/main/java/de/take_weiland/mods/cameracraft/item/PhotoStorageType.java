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
	FILM_B_W_SEALED("filmBwSealed", 24, true),
	FILM_COLOR("filmColor", 32, false),
	FILM_COLOR_SEALED("filmColorSealed", 32, true),
	MEMORY_CARD("memoryCard", 50, false);
	
	public static final PhotoStorageType[] VALUES = values();

	private final String name;
	private final int capacity;
	private final boolean isSealed;
	private final ImageFilter filter;
	
	private PhotoStorageType(String name, int capacity, boolean isSealed) {
		this(name, capacity, isSealed, null);
	}
	
	private PhotoStorageType(String name, int capacity, boolean isSealed, ImageFilter filter) {
		this.name = name;
		this.capacity = capacity;
		this.isSealed = isSealed;
		this.filter = filter;
	}
	
	public int getCapacity() {
		return capacity;
	}
	
	public boolean isSealed() {
		return isSealed;
	}
	
	public PhotoStorage getStorage(ItemStack stack) {
		return PhotoStorages.withCapacity(capacity, isSealed, stack, filter);
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
