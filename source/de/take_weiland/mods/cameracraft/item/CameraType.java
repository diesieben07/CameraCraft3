package de.take_weiland.mods.cameracraft.item;

import net.minecraft.item.ItemStack;
import de.take_weiland.mods.commons.templates.Type;
import de.take_weiland.mods.commons.templates.Typed;
import de.take_weiland.mods.commons.util.Multitypes;

public enum CameraType implements Type<CameraType> {

	FILM("film"),
	DIGITAL("digital");

	private final String name;
	
	private CameraType(String name) {
		this.name = name;
	}
	
	@Override
	public String unlocalizedName() {
		return name;
	}

	@Override
	public Typed<CameraType> getTyped() {
		return CCItem.camera;
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
	public ItemStack stack(int quantity, int meta) {
		throw new IllegalArgumentException();
	}

}
