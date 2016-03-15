package de.take_weiland.mods.cameracraft.client.gui;

import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.cameracraft.gui.ContainerDarkroomTable;
import de.take_weiland.mods.commons.client.AbstractGuiContainer;
import net.minecraft.util.ResourceLocation;

/**
 * @author diesieben07
 */
public class GuiDarkroomTable extends AbstractGuiContainer<ContainerDarkroomTable> {

    public GuiDarkroomTable(ContainerDarkroomTable container) {
        super(container);
    }

    @Override
    protected ResourceLocation provideTexture() {
        return new ResourceLocation(CameraCraft.MOD_ID, "textures/gui/darkroomTable.png");
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

    }
}
