package de.take_weiland.mods.cameracraft.item;

import de.take_weiland.mods.cameracraft.gui.CCGuis;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.awt.*;
import java.util.List;

/**
 * @author Intektor
 */
public class ItemPen extends ItemDraw{

    public ItemPen() {
        super("item.pen");
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
        if(stack.hasTagCompound()) {
            NBTTagCompound nbt = stack.getTagCompound();
            list.add(EnumChatFormatting.RED + "Red: " + nbt.getInteger("Red"));
            list.add(EnumChatFormatting.GREEN + "Green: " + nbt.getInteger("Green"));
            list.add(EnumChatFormatting.BLUE + "Blue: " + nbt.getInteger("Blue"));

        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return "Pen";
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 0;
    }

    @Override
    public int getColorCode(ItemStack stack) {
        int red = ((int)(stack.getTagCompound().getInteger("Red")*25.5) << 16) & 0x00FF0000;
        int green = ((int)(stack.getTagCompound().getInteger("Green")*25.5) << 8) & 0x0000FF00;
        int blue = (int)(stack.getTagCompound().getInteger("Blue")*25.5) & 0x000000FF;
        return 0xFF000000 | red | green | blue;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if(player.isSneaking()) {
            CCGuis.PEN.open(player);
        }
        return itemStack;
    }
}
