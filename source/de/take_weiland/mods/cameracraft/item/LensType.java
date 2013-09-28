package de.take_weiland.mods.cameracraft.item;

import net.minecraft.item.ItemStack;
import de.take_weiland.mods.cameracraft.api.img.ImageFilter;
import de.take_weiland.mods.cameracraft.img.ImageFilters;
import de.take_weiland.mods.commons.templates.Type;
import de.take_weiland.mods.commons.templates.Typed;
import de.take_weiland.mods.commons.util.Multitypes;

public enum LensType implements Type<LensType> {

	RED("red", ImageFilters.RED),
	GREEN("green", ImageFilters.GREEN),
	BLUE("blue", ImageFilters.BLUE),
	YELLOW("yellow", ImageFilters.YELLOW);

	public static final LensType[] VALUES = values();
	
	private final String name;
	private final ImageFilter filter;
	
	private LensType(String name, ImageFilter filter) {
		this.name = name;
		this.filter = filter;
	}
	
	public ImageFilter getFilter() {
		return filter;
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
	public Typed<LensType> getTyped() {
		return CCItem.lenses;
	}

}
