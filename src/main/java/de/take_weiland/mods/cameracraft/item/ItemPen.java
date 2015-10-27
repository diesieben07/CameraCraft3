package de.take_weiland.mods.cameracraft.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * @author Intektor
 */
public class ItemPen extends CCItem{

    public ItemPen() {
        super("item.pen");
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {



    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return "Pen";
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 0;
    }
}
