package de.take_weiland.mods.cameracraft.api;

import net.minecraft.item.ItemStack;

/**
 * @author diesieben07
 */
public interface ChemicalItem extends TrayContainable{

    static ChemicalItem get(ItemStack stack) {
        return CameraCraftApi.get().getCapability(stack, ChemicalItem.class, ChemicalItem::isChemical);
    }

    default boolean isChemical(ItemStack stack) {
        return true;
    }

    int applicationTime(ItemStack stack, ItemStack film);

    ItemStack applyToFilm(ItemStack stack, ItemStack film);

    ItemStack onChemicalUsed(ItemStack stack);

}
