package de.take_weiland.mods.cameracraft.client.gui.state;

import de.take_weiland.mods.commons.inv.SimpleSlot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;

/**
 * @author Intektor
 */
public class GuiStateContainer {

    private int[] slotIDs;
    private ResourceLocation texture;
    public ArrayList<GuiButton> buttonList = new ArrayList<GuiButton>();
    private GuiContainer gui;
    private Container container;
    private boolean allowInvSlots;
    private int guiTop, guiLeft;

    public GuiStateContainer(Container container, GuiContainer gui, ResourceLocation texture, int[] slotIDs, GuiButton[] buttonIDs, boolean allowInvSlots) {
        this(container, gui, texture, slotIDs, buttonIDs, allowInvSlots, (gui.width - 176) / 2, (gui.height - 166) / 2);
    }

    public GuiStateContainer(Container container, GuiContainer gui, ResourceLocation texture, int[] slotIDs, GuiButton[] buttonIDs, boolean allowInvSlots, int guiTop, int guiLeft) {
        this.texture = texture;
        for (GuiButton button : buttonIDs) {
            buttonList.add(button);
        }
        this.gui = gui;
        this.container = container;
        this.slotIDs = slotIDs;
        this.allowInvSlots = allowInvSlots;
        this.guiTop = guiTop;
        this.guiLeft = guiLeft;
    }


    public void renderBackroundState() {
        if (texture != null) {
            Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        }
    }

    public void initState() {
        for (int x = 0; x < container.inventorySlots.size(); x++) {
            if (matchSlot(x)) {
                ((SimpleSlot) container.inventorySlots.get(x)).setNormalPosition();
            } else {
                if (!allowInvSlots) {
                    ((SimpleSlot) container.inventorySlots.get(x)).setDisplayPosition(-1000000, -1000000);
                } else {
                    if (((Slot) container.inventorySlots.get(x)).inventory instanceof InventoryPlayer) {
                        ((SimpleSlot) container.inventorySlots.get(x)).setNormalPosition();
                    }else{
                        ((SimpleSlot) container.inventorySlots.get(x)).setDisplayPosition(-1000000, -1000000);
                    }
                }
            }
        }
    }

    protected boolean matchSlot(int id) {
        for (int i = 0; i < slotIDs.length; i++) {
            if (id == slotIDs[i]) {
                return true;
            }
        }
        return false;
    }

    public ResourceLocation getTexture() {
        return texture;
    }

    public boolean allowsInvSlots() {
        return allowInvSlots;
    }

    public int getGuiLeft() {
        return guiLeft;
    }

    public int getGuiTop() {
        return guiTop;
    }

    public void enableAllButtons(boolean enable) {
        for(GuiButton button : buttonList) {
            button.enabled = enable;
        }
    }
}
