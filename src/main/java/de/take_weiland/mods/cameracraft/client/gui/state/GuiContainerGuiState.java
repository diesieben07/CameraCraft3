package de.take_weiland.mods.cameracraft.client.gui.state;

import de.take_weiland.mods.commons.client.AbstractGuiContainer;
import de.take_weiland.mods.commons.inv.ButtonContainer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.inventory.Container;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author Intektor
 */
public abstract class GuiContainerGuiState<C extends Container> extends AbstractGuiContainer<C> {

    protected ArrayList<GuiStateContainer> guiStates = new ArrayList<>();

    protected int standardGuiLeft, standardGuiTop;

    protected int activeGuiState = 0;

    public GuiContainerGuiState(C container) {
        super(container);
    }

    @Override
    public void initGui() {
        super.initGui();
        standardGuiLeft = (this.width - this.xSize) / 2;
        standardGuiTop = (this.height - this.ySize) / 2;
        guiStates.clear();
    }

    public void setGuiState(int i) {
        exitState(activeGuiState, false);
        activeGuiState = i;
        initGuiState(i);
    }

    public int getActiveGuiStateNumber() {
        return activeGuiState;
    }

    @Override
    protected final void actionPerformed(GuiButton button) {
        buttonPressed(activeGuiState, button);
        if (container instanceof ButtonContainer) {
            ((ButtonContainer) container).triggerButton(getCurrentGuiState().getUniqueButtonID(button.id));
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        buttonList = getCurrentGuiState().buttonList;
    }

    protected abstract void buttonPressed(int activeGuiState, GuiButton button);

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        guiStates.get(activeGuiState).renderBackroundState();
        if (guiStates.get(activeGuiState).getTexture() != null) {
            drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        }
    }

    protected GuiStateContainer getCurrentGuiState() {
        return guiStates.get(activeGuiState);
    }

    protected void initGuiState() {
        initGuiState(getActiveGuiStateNumber());
    }

    private void initGuiState(int state) {
        buttonList = guiStates.get(state).buttonList;
        guiStates.get(state).initState();
        this.guiTop = guiStates.get(state).getGuiTop() != -1 ? guiStates.get(state).getGuiTop() : standardGuiTop;
        this.guiLeft = guiStates.get(state).getGuiLeft() != -1 ? guiStates.get(state).getGuiLeft() : standardGuiLeft;
        this.xSize = guiStates.get(state).getxSize();
        this.ySize = guiStates.get(state).getySize();
        initState(state);
    }


    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
    }

    protected abstract void initState(int state);

    protected abstract void exitState(int state, boolean exitGui);

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        System.out.println(keyCode);
        if (!shouldExitOnKeyboardType(typedChar, keyCode)) {
            super.keyTyped(typedChar, keyCode);
        } else {
            exitState(activeGuiState, true);
        }
    }

    protected long lastMouseClicked;

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (lastMouseClicked > System.currentTimeMillis() - (int) Toolkit.getDefaultToolkit().getDesktopProperty("awt.multiClickInterval")) {
            doubleClick(mouseX, mouseY, mouseButton);
        }
        lastMouseClicked = System.currentTimeMillis();
    }

    protected void doubleClick(int mouseX, int mouseY, int mouseButton) {

    }

    protected abstract boolean shouldExitOnKeyboardType(char typedChar, int keyCode);
}
