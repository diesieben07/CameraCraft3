package de.take_weiland.mods.cameracraft.api.photo;

import net.minecraft.item.ItemStack;

public interface FilmItem extends PhotoStorageItem {

	/**
	 * rewind the given film, does nothing if it's not this item or already rewinded
	 * @param stack the film to rewind
	 * @return the rewinded film
	 */
	ItemStack rewind(ItemStack stack);
	
}
