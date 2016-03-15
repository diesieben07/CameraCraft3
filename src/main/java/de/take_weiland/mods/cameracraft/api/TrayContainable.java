package de.take_weiland.mods.cameracraft.api;

import net.minecraft.item.ItemStack;

/**
 * @author diesieben07
 */
public interface TrayContainable {

    default String getDisplayInTray(ItemStack stack) {
        return stack.getDisplayName();
    }

}
