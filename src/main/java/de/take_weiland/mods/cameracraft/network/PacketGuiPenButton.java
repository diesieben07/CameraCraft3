package de.take_weiland.mods.cameracraft.network;

import de.take_weiland.mods.commons.net.MCDataInput;
import de.take_weiland.mods.commons.net.MCDataOutput;
import de.take_weiland.mods.commons.net.Packet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;


/**
 * @author Intektor
 */
public class PacketGuiPenButton implements Packet {

    int red, green, blue;

    public PacketGuiPenButton(NBTTagCompound nbt) {
        red = nbt.getInteger("Red");
        green = nbt.getInteger("Green");
        blue = nbt.getInteger("Blue");
    }

    public PacketGuiPenButton(MCDataInput in) {
        red = in.readInt();
        green = in.readInt();
        blue = in.readInt();
    }


    @Override
    public void writeTo(MCDataOutput out) {
        out.writeInt(red);
        out.writeInt(green);
        out.writeInt(blue);
    }

    public void handle(EntityPlayer player) {
        ItemStack stack = player.getCurrentEquippedItem();
        NBTTagCompound nbt = stack.getTagCompound();
        nbt.setInteger("Red", red);
        nbt.setInteger("Green", green);
        nbt.setInteger("Blue", blue);

    }


}
