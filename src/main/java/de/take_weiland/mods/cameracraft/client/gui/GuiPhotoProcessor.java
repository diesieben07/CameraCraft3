package de.take_weiland.mods.cameracraft.client.gui;

import net.minecraft.util.ResourceLocation;
import de.take_weiland.mods.cameracraft.gui.ContainerPhotoProcessor;
import de.take_weiland.mods.cameracraft.tileentity.TilePhotoProcessor;
import de.take_weiland.mods.commons.client.AbstractGuiContainer;
import de.take_weiland.mods.commons.client.Rendering;

public class GuiPhotoProcessor extends AbstractGuiContainer<TilePhotoProcessor, ContainerPhotoProcessor> {

	private int ticker;
	
	public GuiPhotoProcessor(ContainerPhotoProcessor container) {
		super(container);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		
		int offset = ticker % 29;
		drawTexturedModalRect(guiLeft + 34, guiTop + 21 + 28 - offset, 176, 31 + 28 - offset, 11, offset);
		
		Rendering.drawFluidStack(container.inventory().tank, guiLeft + 152, guiTop + 9, 16, 59);
	}

	@Override
	public ResourceLocation provideTexture() {
		return new ResourceLocation("cameracraft", "textures/gui/photoProcessor.png");
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		if (!container.inventory().isProcessing()) {
			ticker = 0;
		} else {
			ticker++;
		}
	}

}
