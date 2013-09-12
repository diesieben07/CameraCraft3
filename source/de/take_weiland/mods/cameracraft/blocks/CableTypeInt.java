package de.take_weiland.mods.cameracraft.blocks;

import net.minecraft.item.ItemStack;
import de.take_weiland.mods.cameracraft.api.cable.CableType;
import de.take_weiland.mods.commons.templates.Type;

public enum CableTypeInt implements Type {
	POWER("power"),
	DATA("data");

	private final String name;
	
	private CableTypeInt(String name) {
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
	
	public CableType toApiForm() {
		return CableType.values()[ordinal()];
	}
	
	public static CableTypeInt fromApi(CableType apiForm) {
		return values()[apiForm.ordinal()];
	}

	@Override
	public ItemStack stack() {
		return stack(1);
	}

	@Override
	public ItemStack stack(int quantity) {
		return CCBlock.cable.stack(quantity, ordinal());
	}

	@Override
	public ItemStack stack(int quantity, int meta) {
		throw new IllegalArgumentException();
	}

}
