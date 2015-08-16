package de.take_weiland.mods.cameracraft.api.printer;

import net.minecraft.item.ItemStack;

public interface InkItem {

	boolean isInk(ItemStack stack);
	
	int getAmount(ItemStack stack);
	
	void setAmount(ItemStack stack, int newAmount);
	
	InkItem.Color getColor(ItemStack stack);
	
	enum Color {
		
		CYAN,
		MAGENTA,
		YELLOW,
		BLACK
		
	}
	
}
