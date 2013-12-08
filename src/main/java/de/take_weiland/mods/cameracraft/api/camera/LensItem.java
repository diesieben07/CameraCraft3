package de.take_weiland.mods.cameracraft.api.camera;

import net.minecraft.item.ItemStack;
import de.take_weiland.mods.cameracraft.api.img.ImageFilter;

public interface LensItem {

	ImageFilter getFilter(ItemStack stack);
	
}
