package de.take_weiland.mods.cameracraft.api;

import net.minecraft.item.ItemStack;

/**
 * @author diesieben07
 */
public interface TrayItem {

    static TrayItem get(ItemStack stack) {
        return CameraCraftApi.get().getCapability(stack, TrayItem.class, TrayItem::isTray);
    }

    default boolean isTray(ItemStack stack) {
        return true;
    }

    ItemStack getContainedChemical(ItemStack stack);

    ItemStack setContainedChemical(ItemStack stack, ItemStack chemical);

    ItemStack getContainedFilm(ItemStack stack);

    ItemStack setContainedFilm(ItemStack stack, ItemStack film);

}
