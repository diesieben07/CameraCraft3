package de.take_weiland.mods.cameracraft.client.gui;

import de.take_weiland.mods.cameracraft.gui.ContainerScanner;
import de.take_weiland.mods.commons.client.AbstractGuiContainer;
import net.minecraft.util.ResourceLocation;

public class GuiScanner extends AbstractGuiContainer<ContainerScanner> {

	public GuiScanner(ContainerScanner container) {
		super(container);
	}

	@Override
	protected ResourceLocation provideTexture() {
		return new ResourceLocation("cameracraft:textures/gui/scanner.png");
	}

}
