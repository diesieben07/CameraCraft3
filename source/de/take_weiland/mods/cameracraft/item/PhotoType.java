package de.take_weiland.mods.cameracraft.item;

import net.minecraft.item.ItemStack;
import de.take_weiland.mods.commons.templates.Type;
import de.take_weiland.mods.commons.templates.Typed;
import de.take_weiland.mods.commons.util.Multitypes;

public enum PhotoType implements Type<PhotoType> {
	;

	public static final PhotoType[] VALUES = values();
	
	private final String name;
	
	private PhotoType(String name) {
		this.name = name;
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
		return Multitypes.is(stack, this);
	}

	@Override
	public String unlocalizedName() {
		return name;
	}

	@Override
	public Typed<PhotoType> getTyped() {
		return CCItem.photo;
	}

}
