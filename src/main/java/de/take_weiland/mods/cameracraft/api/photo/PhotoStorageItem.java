package de.take_weiland.mods.cameracraft.api.photo;

import net.minecraft.item.ItemStack;

public interface PhotoStorageItem {
	
	PhotoStorage getPhotoStorage(ItemStack stack);

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
	
	/**
	 * @param stack
	 * @return whether this PhotoStorage can be processed in the PhotoProcessor
	 */
	boolean canBeProcessed(ItemStack stack);
	
	/**
	 * get the result of processing this PhotoStorage in the PhotoProcessor
	 * @param stack
	 * @return
	 */
	ItemStack process(ItemStack stack);

	/**
	 * @return whether this PhotoStorage can be scanned in a Scanner
	 */
	boolean isScannable(ItemStack stack);
	
}
