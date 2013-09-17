package de.take_weiland.mods.cameracraft.client.gui;

import net.minecraft.util.ResourceLocation;
import de.take_weiland.mods.cameracraft.gui.ContainerCamera;
import de.take_weiland.mods.cameracraft.inv.InventoryCamera;
import de.take_weiland.mods.commons.client.AbstractGuiContainer;

public class GuiCamera extends AbstractGuiContainer<InventoryCamera, ContainerCamera> {

	public GuiCamera(ContainerCamera container) {
		super(container);
	}

	@Override
	protected ResourceLocation provideTexture() {
		return new ResourceLocation("cameracraft:textures/gui/filmCamera.png");
	}

}
