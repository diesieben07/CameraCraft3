package de.take_weiland.mods.cameracraft.api.energy;

import net.minecraft.item.ItemStack;

public interface BatteryHandler {

	boolean isBattery(ItemStack stack);
	
	int getCharge(ItemStack stack);
	
	boolean isRechargable(ItemStack stack);
	
	int getCapacity(ItemStack stack);
	
	/**
	 * tries to charge the Battery with <code>amount</code> units of energy<br>
	 * does nothing if this battery is not rechargable
	 * @param stack the battery
	 * @param amount the amount actually charged
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
	
}
