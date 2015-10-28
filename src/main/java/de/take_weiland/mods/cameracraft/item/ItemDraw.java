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

    public abstract int getColorCode(NBTTagCompound nbt);

    @Override
    public void onCreated(ItemStack stack, World world, EntityPlayer player) {
        stack.setTagCompound(new NBTTagCompound());
    }
}
