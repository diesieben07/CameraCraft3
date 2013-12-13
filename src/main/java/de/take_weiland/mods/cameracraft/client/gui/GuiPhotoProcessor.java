package de.take_weiland.mods.cameracraft.client.gui;

import net.minecraft.util.ResourceLocation;
import de.take_weiland.mods.cameracraft.gui.ContainerPhotoProcessor;
import de.take_weiland.mods.cameracraft.tileentity.TilePhotoProcessor;
import de.take_weiland.mods.commons.client.AbstractGuiContainer;
import de.take_weiland.mods.commons.client.Rendering;

public class GuiPhotoProcessor extends AbstractGuiContainer<TilePhotoProcessor, ContainerPhotoProcessor> {

	private static final ResourceLocation TEXTURE = new ResourceLocation("cameracraft", "textures/gui/photoProcessor.png");

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
		Rendering.drawFluidStack(container.inventory().tank, guiLeft + 152, guiTop + 9, 16, 59);
	}

	@Override
	public ResourceLocation provideTexture() {
		return TEXTURE;
	}

}
