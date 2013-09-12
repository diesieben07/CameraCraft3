package de.take_weiland.mods.cameracraft.blocks;

import net.minecraft.item.ItemStack;
import de.take_weiland.mods.commons.templates.Type;

public enum OreType implements Type {
	TIN("tin"),
	ALKALINE("alkaline"),
	BLOCK_TIN("blockTin");

	private final String name;
	
	private OreType(String name) {
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
		return CCBlock.ores.stack(quantity, ordinal());
	}

	@Override
	public ItemStack stack(int quantity, int meta) {
		throw new IllegalArgumentException();
	}

}
