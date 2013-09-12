package de.take_weiland.mods.cameracraft.blocks;

import net.minecraft.item.ItemStack;
import de.take_weiland.mods.commons.templates.Type;
import de.take_weiland.mods.commons.templates.Typed;
import de.take_weiland.mods.commons.util.Multitypes;

public enum OreType implements Type<OreType> {
	
	TIN("tin"),
	ALKALINE("alkaline"),
	BLOCK_TIN("blockTin");

	private final String name;
	
	private OreType(String name) {
		this.name = name;
	}
	
	@Override
	public String unlocalizedName() {
		return name;
	}

	@Override
	public Typed<OreType> getTyped() {
		return CCBlock.ores;
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
