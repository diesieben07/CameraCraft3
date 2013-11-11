package de.take_weiland.mods.cameracraft.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import de.take_weiland.mods.commons.templates.Metadata.BlockMeta;
import de.take_weiland.mods.commons.util.ItemStacks;

public enum CableType implements BlockMeta, de.take_weiland.mods.cameracraft.api.cable.CableType {
	POWER("power"),
	DATA("data");

	public static final CableType[] VALUES = values();
	
	private final String name;
	
	private CableType(String name) {
		this.name = name;
	}
	
	@Override
	public Block getBlock() {
		return CCBlock.cable;
	}
	
	@Override
	public String unlocalizedName() {
		return name;
	}

	@Override
	public boolean isThis(ItemStack stack) {
		return ItemStacks.is(stack, this);
	}
	
	@Override
	public boolean isThis(IBlockAccess world, int x, int y, int z) {
		return CCBlock.machines.blockID == world.getBlockId(x, y, z) && ordinal() == world.getBlockMetadata(x, y, z);
	}

	@Override
	public boolean isData() {
		return this == DATA;
	}

	@Override
	public boolean isPower() {
		return this == POWER;
	}

}
