package de.take_weiland.mods.cameracraft.api.photo;

import net.minecraft.item.ItemStack;

public interface PhotoStorageItem {

	PhotoStorage getStorage(ItemStack stack);
	
	boolean isSealed(ItemStack stack);
	
	ItemStack unseal(ItemStack sealed);
	
	/**
	 * determine if this PhotoStorage can be rewinded (usually films)
	 * @param stack
	 * @return
	 */
	boolean canRewind(ItemStack stack);
	
	/**
	 * rewind the given film, does nothing if it's not this item or already rewinded
	 * @param stack the film to rewind
	 * @return the rewinded film
	 */
	ItemStack rewind(ItemStack stack);
	
}
