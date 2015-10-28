package de.take_weiland.mods.cameracraft.item;

import net.minecraft.item.ItemStack;

/**
 * @author Intektor
 */
public class ItemRubber extends ItemDraw {

    public ItemRubber() {
        super("item.rubber");
    }

    @Override
    public int getColorCode(ItemStack stack) {
        return 0;
    }
}
