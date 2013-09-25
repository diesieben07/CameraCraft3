package de.take_weiland.mods.cameracraft.blocks;

import net.minecraft.item.ItemStack;
import de.take_weiland.mods.commons.templates.Type;
import de.take_weiland.mods.commons.templates.Typed;

public enum CableType implements Type<CableType>, de.take_weiland.mods.cameracraft.api.cable.CableType {
	POWER("power"),
	DATA("data");

	private final String name;
	
	private CableType(String name) {
		this.name = name;
	}
	
	@Override
	public Typed<CableType> getTyped() {
		return CCBlock.cable;
	}
	
	@Override
	public String unlocalizedName() {
		return name;
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

	@Override
	public boolean isData() {
		return this == DATA;
	}

	@Override
	public boolean isPower() {
		return this == POWER;
	}

	@Override
	public boolean is(de.take_weiland.mods.cameracraft.api.cable.CableType other) {
		return other == this;
	}

}
