package de.take_weiland.mods.cameracraft.item;

import net.minecraft.item.ItemStack;
import de.take_weiland.mods.cameracraft.api.photo.PhotoStorage;
import de.take_weiland.mods.cameracraft.photo.PhotoStorages;
import de.take_weiland.mods.commons.templates.Type;
import de.take_weiland.mods.commons.templates.Typed;
import de.take_weiland.mods.commons.util.Multitypes;

public enum PhotoStorageType implements Type<PhotoStorageType> {

	FILM_B_W("film_bw", 24, false),
	FILM_B_W_PROCESSED("film_bw_processed", 24, true),
	FILM_COLOR("film_color", 32, false),
	FILM_COLOR_PROCESSED("film_color_processed", 32, true),
	MEMORY_CARD("memoryCard", 50, false);
	
	public static final PhotoStorageType[] VALUES = values();

	private final String name;
	private final int capacity;
	private final boolean isSealed;
	
	private PhotoStorageType(String name, int capacity, boolean isSealed) {
		this.name = name;
		this.capacity = capacity;
		this.isSealed = isSealed;
	}
	
	public int getCapacity() {
		return capacity;
	}
	
	public PhotoStorage getStorage(ItemStack stack) {
		return PhotoStorages.withCapacity(capacity, isSealed, stack);
	}

	@Override
	public String unlocalizedName() {
		return name;
	}

	@Override
	public Typed<PhotoStorageType> getTyped() {
		return CCItem.photoStorage;
	}
	
	@Override
	public ItemStack stack() {
		return stack(1);
	}

	@Override
	public ItemStack stack(int quantity) {
		return Multitypes.stack(this, quantity);
	}

	@Override
	public boolean isThis(ItemStack stack) {
		return stack != null && getTyped().isThis(stack) && stack.getItemDamage() == ordinal();
	}

}
