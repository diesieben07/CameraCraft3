package de.take_weiland.mods.cameracraft.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * @author Intektor
 */
public abstract class ItemDraw extends CCItem {

    public ItemDraw(String name) {
        super(name);
        this.setMaxStackSize(1);
    }

    public abstract int getColorCode(ItemStack stack);

    @Override
    public void onCreated(ItemStack stack, World world, EntityPlayer player) {
        stack.setTagCompound(new NBTTagCompound());
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int p_77663_4_, boolean p_77663_5_) {
        if(!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
    }
}
