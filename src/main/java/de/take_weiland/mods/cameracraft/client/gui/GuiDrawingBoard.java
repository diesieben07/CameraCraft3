package de.take_weiland.mods.cameracraft.client.gui;

import de.take_weiland.mods.cameracraft.gui.ContainerDrawingBoard;
import de.take_weiland.mods.commons.client.AbstractGuiContainer;
import net.minecraft.util.ResourceLocation;

/**
 * @author Intektor
 */
public class GuiDrawingBoard extends AbstractGuiContainer<ContainerDrawingBoard> {

    public GuiDrawingBoard(ContainerDrawingBoard container) {
        super(container);
    }

    @Override
    protected ResourceLocation provideTexture() {
        return null;
    }


}
