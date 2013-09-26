package de.take_weiland.mods.cameracraft.blocks;

import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import de.take_weiland.mods.commons.templates.Type;
import de.take_weiland.mods.commons.templates.Typed;
import de.take_weiland.mods.commons.util.Multitypes;

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
		return Multitypes.stack(this, quantity);
	}

	@Override
	public boolean isThis(ItemStack stack) {
		return Multitypes.is(stack, this);
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
