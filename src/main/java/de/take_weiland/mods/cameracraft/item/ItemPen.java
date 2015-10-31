package de.take_weiland.mods.cameracraft.item;

import de.take_weiland.mods.cameracraft.gui.CCGuis;
import de.take_weiland.mods.commons.util.ItemStacks;
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
public class ItemPen extends ItemDraw {

    public ItemPen() {
        super("item.pen");
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
        if (stack.hasTagCompound()) {
            NBTTagCompound nbt = stack.getTagCompound();
            list.add(EnumChatFormatting.RED + "Red: " + nbt.getInteger("Red"));
            list.add(EnumChatFormatting.GREEN + "Green: " + nbt.getInteger("Green"));
            list.add(EnumChatFormatting.BLUE + "Blue: " + nbt.getInteger("Blue"));

        }
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 1;
    }

    @Override
    public int getColorCode(NBTTagCompound nbt) {
        if(nbt != null) {
            int red = ((int) (nbt.getInteger("Red") * 25.5) << 16) & 0x00FF0000;
            int green = ((int) (nbt.getInteger("Green") * 25.5) << 8) & 0x0000FF00;
            int blue = (int) (nbt.getInteger("Blue") * 25.5) & 0x000000FF;
            return 0xFF000000 | red | green | blue;
        }else{
            return Color.black.getRGB();
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (player.isSneaking() && !player.capabilities.isFlying) {
            ItemStacks.getNbt(itemStack);
            if (itemStack.hasTagCompound()) {
                CCGuis.PEN.open(player);
            }
        }
        return itemStack;
    }
}
