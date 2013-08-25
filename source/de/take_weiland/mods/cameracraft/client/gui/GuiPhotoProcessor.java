package de.take_weiland.mods.cameracraft.client.gui;

import net.minecraft.util.ResourceLocation;
import de.take_weiland.mods.cameracraft.gui.ContainerPhotoProcessor;
import de.take_weiland.mods.cameracraft.tileentity.TilePhotoProcessor;
import de.take_weiland.mods.commons.client.AbstractGuiContainer;
import de.take_weiland.mods.commons.client.GuiRendering;

public class GuiPhotoProcessor extends AbstractGuiContainer<TilePhotoProcessor, ContainerPhotoProcessor> {

	public GuiPhotoProcessor(ContainerPhotoProcessor container) {
		super(container);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		GuiRendering.drawFluidStack(container.inventory().tank, 152, 9, 16, 59, this);
	}

	@Override
	public ResourceLocation provideTexture() {
		return new ResourceLocation("cameracraft", "textures/gui/photoProcessor.png");
	}

}
