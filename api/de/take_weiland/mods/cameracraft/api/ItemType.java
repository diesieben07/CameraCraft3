package de.take_weiland.mods.cameracraft.api;

import net.minecraft.item.ItemStack;

public interface ItemType<T extends ItemType<T>> {

	boolean isThis(ItemStack stack);
	
}
