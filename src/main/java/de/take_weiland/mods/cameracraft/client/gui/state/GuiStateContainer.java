package de.take_weiland.mods.cameracraft.client.gui.state;

import de.take_weiland.mods.commons.inv.SimpleGuiButton;
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
    public ArrayList<GuiButton> buttonList = new ArrayList<>();
    private GuiContainer gui;
    private Container container;
    private boolean allowInvSlots;
    private int guiTop, guiLeft;
    private int ID, xSize, ySize;
    private final int[] standardHideButtons, standardHideSlots;

    public GuiStateContainer(int id, Container container, GuiContainer gui, ResourceLocation texture, int[] slotIDs, GuiButton[] buttonIDs, boolean allowInvSlots) {
        this(id, container, gui, texture, slotIDs, buttonIDs, allowInvSlots, -1, -1);
    }

    public GuiStateContainer(int id, Container container, GuiContainer gui, ResourceLocation texture, int[] slotIDs, GuiButton[] buttonIDs, boolean allowInvSlots, int guiTop, int guiLeft) {
        this(id, container, gui, texture, slotIDs, buttonIDs, allowInvSlots, guiTop, guiLeft, new int[0], new int[0]);
    }

    public GuiStateContainer(int id, Container container, GuiContainer gui, ResourceLocation texture, int[] slotIDs, GuiButton[] buttonIDs, boolean allowInvSlots, int guiTop, int guiLeft, int[] standardHideButtons, int[] standardHideSlots) {
        this(id, container, gui, texture, slotIDs, buttonIDs, allowInvSlots, guiTop, guiLeft, standardHideButtons, standardHideSlots, 176, 166);
    }

    public GuiStateContainer(int id, Container container, GuiContainer gui, ResourceLocation texture, int[] slotIDs, GuiButton[] buttonIDs, boolean allowInvSlots, int guiTop, int guiLeft, int[] standardHideButtons, int[] standardHideSlots, int textureWidth, int textureHeight) {
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
        this.ID = id;
        this.standardHideButtons = standardHideButtons;
        this.standardHideSlots = standardHideSlots;
        this.xSize = textureWidth;
        this.ySize = textureHeight;
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
                    } else {
                        ((SimpleSlot) container.inventorySlots.get(x)).setDisplayPosition(-1000000, -1000000);
                    }
                }
            }
            for (int i : standardHideSlots) {
                if (x == i) {
                    ((SimpleSlot) container.inventorySlots.get(x)).setDisplayPosition(-1000000, -1000000);
                }
            }
        }
        for (int x = 0; x < buttonList.size(); x++) {
            if (buttonList.get(x) instanceof SimpleGuiButton) {
                for (int i : standardHideButtons) {
                    if (i == x) {
                        ((SimpleGuiButton) buttonList.get(x)).hideButton();
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
        for (GuiButton button : buttonList) {
            button.enabled = enable;
        }
    }

    public int getID() {
        return ID;
    }

    public int getUniqueButtonID(int buttonID) {
        return (ID << 16) | (buttonID);
    }

    public void hideButton(int x, boolean hide) {
        if (hide) {
            ((SimpleGuiButton) buttonList.get(x)).hideButton();
        } else {
            ((SimpleGuiButton) buttonList.get(x)).setNormalPosition();
        }
    }

    public int getxSize() {
        return xSize;
    }

    public int getySize() {
        return ySize;
    }
}
