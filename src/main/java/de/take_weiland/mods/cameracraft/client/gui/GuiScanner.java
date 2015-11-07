package de.take_weiland.mods.cameracraft.client.gui;

import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.cameracraft.api.photo.PhotoStorage;
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

    private GuiButton buttonScan;

    public GuiScanner(ContainerScanner container) {
        super(container);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        super.initGui();
        buttonList.add(buttonScan = new GuiButtonImage(BUTTON_SCAN, guiLeft + 125, guiTop + 33, 20, 20, new ResourceLocation(CameraCraft.MOD_ID, "textures/gui/controls.png"), 24, 0));
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        buttonScan.enabled = container.inventory().getPhotoIndex() == TileScanner.NOT_SCANNING;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

        int scanTimer = container.inventory().getScanTimer();
        int photoIndex = container.inventory().getPhotoIndex();

        if (photoIndex != TileScanner.NOT_SCANNING) {
            int scaledWidth = (int) ((float) (TileScanner.TIME_PER_PHOTO - scanTimer) / TileScanner.TIME_PER_PHOTO * 24);

            PhotoStorage source = container.inventory().getSource();
            int numPhotos = source == null ? 0 : source.size();

            RenderHelper.disableStandardItemLighting();
            bindTexture();
            Rendering.drawTexturedQuad(guiLeft + 76, guiTop + 35, scaledWidth, 17, 176, 0, scaledWidth, 17, 256);

            if (numPhotos != 0) {
                int idx = container.inventory().getPhotoIndex() + 1;
                int scaledOverallWidth = (int) ((float) idx / numPhotos * 24);
                Rendering.drawColoredQuad(guiLeft + 76, guiTop + 55, scaledOverallWidth, 2, 0xff0000);
            }
        }
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
