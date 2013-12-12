package de.take_weiland.mods.cameracraft.api.energy;

import net.minecraft.item.ItemStack;

public interface BatteryHandler {

	boolean handles(ItemStack battery);
	
	int getCharge(ItemStack stack);
	
	boolean isRechargable(ItemStack stack);
	
	int getCapacity(ItemStack stack);
	
	/**
	 * tries to charge the Battery with <code>amount</code> units of energy
	 * @param stack the battery
	 * @param amount the 
	 * @return
	 */
	int charge(ItemStack stack, int amount);
	
	/**
	 * Tries to drain <code>amount</code> units of energy from the Battery	
	 * @param stack
	 * @param amount
	 * @return the actual amount drained
	 */
	int drain(ItemStack stack, int amount);
	
	int setCharge(ItemStack stack, int newCharge);
	
}
