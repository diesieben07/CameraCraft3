package de.take_weiland.mods.cameracraft.api.photo;

import net.minecraft.item.ItemStack;

public interface PhotoItem extends PhotoStorageItem {

	boolean isNamed(ItemStack stack);
	
	String getName(ItemStack stack);
	
}
