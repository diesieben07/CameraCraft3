package de.take_weiland.mods.cameracraft.client.gui;

import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.cameracraft.gui.ContainerScanner;
import de.take_weiland.mods.cameracraft.tileentity.TileScanner;
import de.take_weiland.mods.commons.client.AbstractGuiContainer;
import de.take_weiland.mods.commons.client.GuiButtonImage;
import de.take_weiland.mods.commons.client.Rendering;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;

public class GuiScanner extends AbstractGuiContainer<ContainerScanner> {

    private static final int BUTTON_SCAN = 0;

	public GuiScanner(ContainerScanner container) {
		super(container);
	}

	@SuppressWarnings("unchecked")
    @Override
	public void initGui() {
        super.initGui();
		buttonList.add(new GuiButtonImage(BUTTON_SCAN, 0, 0, 20, 20, new ResourceLocation(CameraCraft.MOD_ID, "textures/gui/controls.png"), 24, 0));
	}

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        int scaledWidth = (int) ((float) (TileScanner.TIME_PER_PHOTO - container.inventory().getScanTimer()) / TileScanner.TIME_PER_PHOTO * 24);

        RenderHelper.disableStandardItemLighting();
        bindTexture();
        Rendering.drawTexturedQuad(guiLeft + 76, guiTop + 35, scaledWidth, 17, 176, 0, scaledWidth, 17, 256);

    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case BUTTON_SCAN:
                container.triggerButton(0);
                break;
        }
    }

    @Override
	protected ResourceLocation provideTexture() {
		return new ResourceLocation("cameracraft:textures/gui/scanner.png");
	}

}
