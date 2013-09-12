package de.take_weiland.mods.cameracraft.blocks;

import net.minecraft.item.ItemStack;
import de.take_weiland.mods.cameracraft.api.cable.CableType;
import de.take_weiland.mods.commons.templates.Type;
import de.take_weiland.mods.commons.templates.Typed;

public enum CableTypeInt implements Type<CableTypeInt> {
	POWER("power"),
	DATA("data");

	private final String name;
	
	private CableTypeInt(String name) {
		this.name = name;
	}
	
	@Override
	public Typed<CableTypeInt> getTyped() {
		return CCBlock.cable;
	}
	
	@Override
	public String unlocalizedName() {
		return name;
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
