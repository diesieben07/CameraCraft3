package de.take_weiland.mods.cameracraft.client.gui;

import de.take_weiland.mods.cameracraft.network.PacketGuiPenButton;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.awt.*;

/**
 * @author Intektor
 */
public class GuiPen extends GuiScreen {

    NBTTagCompound nbt;

    public GuiPen() {

    }

    @Override
    public void initGui() {
        nbt = (NBTTagCompound)mc.thePlayer.getCurrentEquippedItem().getTagCompound().copy();
        GuiButton button[] = new GuiButton[]{
                new GuiButton(0, width / 2 - 60, height / 2 - 25, 20, 20, "-"),
                new GuiButton(1, width / 2 + 40, height / 2 - 25, 20, 20, "+"),

                new GuiButton(2, width / 2 - 60, height / 2 - 5, 20, 20, "-"),
                new GuiButton(3, width / 2 + 40, height / 2 - 5, 20, 20, "+"),

                new GuiButton(4, width / 2 - 60, height / 2 + 15, 20, 20, "-"),
                new GuiButton(5, width / 2 + 40, height / 2 + 15, 20, 20, "+"),
        };

        for (GuiButton but : button) {
            buttonList.add(but);
        }

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (nbt != null) {
            drawCenteredString(mc.fontRendererObj, "Red: " + nbt.getInteger("Red"), width / 2, height / 2 - 20, Color.red.getRGB());
            drawCenteredString(mc.fontRendererObj, "Green: " + nbt.getInteger("Green"), width / 2, height / 2, Color.green.getRGB());
            drawCenteredString(mc.fontRendererObj, "Blue: " + nbt.getInteger("Blue"), width / 2, height / 2 + 20, Color.blue.getRGB());
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        buttonActivate(mc.thePlayer, button.id);
    }

    public void buttonActivate(EntityPlayer player, int id) {
        ItemStack stack = player.getCurrentEquippedItem();
        switch (id) {
            case 0:
                if (nbt.getInteger("Red") > 0) {
                    nbt.setInteger("Red", nbt.getInteger("Red") - 1);
                }
                break;
            case 1:
                if (nbt.getInteger("Red") < 10) {
                    nbt.setInteger("Red", nbt.getInteger("Red") + 1);
                }
                break;
            case 2:
                if (nbt.getInteger("Green") > 0) {
                    nbt.setInteger("Green", nbt.getInteger("Green") - 1);
                }
                break;
            case 3:
                if (nbt.getInteger("Green") < 10) {
                    nbt.setInteger("Green", nbt.getInteger("Green") + 1);
                }
                break;
            case 4:
                if (nbt.getInteger("Blue") > 0) {
                    nbt.setInteger("Blue", nbt.getInteger("Blue") - 1);
                }
                break;
            case 5:
                if (nbt.getInteger("Blue") < 10) {
                    nbt.setInteger("Blue", nbt.getInteger("Blue") + 1);
                }
                break;
        }
    }

    @Override
    public void onGuiClosed() {
        new PacketGuiPenButton(nbt).sendToServer();
    }
}
