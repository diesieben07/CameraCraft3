package de.take_weiland.mods.cameracraft.api.photo;

import net.minecraft.item.ItemStack;

public interface PhotoStorageItem {

	PhotoStorage getStorage(ItemStack stack);
	
}
