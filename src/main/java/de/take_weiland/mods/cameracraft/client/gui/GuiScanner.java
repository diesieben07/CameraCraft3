package de.take_weiland.mods.cameracraft.client.gui;

import net.minecraft.util.ResourceLocation;
import de.take_weiland.mods.cameracraft.gui.ContainerScanner;
import de.take_weiland.mods.cameracraft.tileentity.TileScanner;
import de.take_weiland.mods.commons.client.AbstractGuiContainer;

public class GuiScanner extends AbstractGuiContainer<TileScanner, ContainerScanner> {

	public GuiScanner(ContainerScanner container) {
		super(container);
	}

	@Override
	protected ResourceLocation provideTexture() {
		return new ResourceLocation("cameracraft:textures/gui/scanner.png");
	}

}
