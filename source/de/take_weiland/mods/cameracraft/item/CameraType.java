package de.take_weiland.mods.cameracraft.item;

import net.minecraft.item.ItemStack;
import de.take_weiland.mods.commons.templates.Type;

public enum CameraType implements Type {

	FILM("film"),
	DIGITAL("digital");

	private final String name;
	
	private CameraType(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getMeta() {
		return ordinal();
	}

	@Override
	public ItemStack stack() {
		return stack(1);
	}

	@Override
	public ItemStack stack(int quantity) {
		return CCItem.camera.stack(quantity, ordinal());
	}

	@Override
	public ItemStack stack(int quantity, int meta) {
		throw new IllegalArgumentException();
	}
	
}
