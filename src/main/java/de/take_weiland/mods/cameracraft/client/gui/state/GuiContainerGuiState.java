package de.take_weiland.mods.cameracraft.client.gui.state;

import de.take_weiland.mods.commons.client.AbstractGuiContainer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.inventory.Container;

import java.util.ArrayList;

/**
 * @author Intektor
 */
public abstract class GuiContainerGuiState<C extends Container> extends AbstractGuiContainer<C> {

    protected ArrayList<GuiStateContainer> guiStates = new ArrayList<GuiStateContainer>();

    protected int activeGuiState = 0;

    public GuiContainerGuiState(C container) {
        super(container);
    }

    @Override
    public void initGui() {
        super.initGui();
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
        this.guiTop = guiStates.get(state).getGuiTop();
        this.guiLeft = guiStates.get(state).getGuiLeft();
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
        if (keyCode != 1) {
            super.keyTyped(typedChar, keyCode);
        }else{
            exitState(activeGuiState, true);
        }
    }
}
