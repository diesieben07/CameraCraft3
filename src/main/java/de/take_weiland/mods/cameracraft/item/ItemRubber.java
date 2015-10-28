package de.take_weiland.mods.cameracraft.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author Intektor
 */
public class ItemRubber extends ItemDraw {

    public ItemRubber() {
        super("item.rubber");
    }

    @Override
    public int getColorCode(NBTTagCompound nbt) {
        return 0;
    }
}
