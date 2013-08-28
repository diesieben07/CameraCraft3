package de.take_weiland.mods.cameracraft.client.gui;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import de.take_weiland.mods.cameracraft.gui.ContainerItemTranslator;
import de.take_weiland.mods.cameracraft.tileentity.TileItemMutator;
import de.take_weiland.mods.commons.client.AbstractGuiContainer;

public class GuiItemTranslator extends AbstractGuiContainer<TileItemMutator, ContainerItemTranslator> {

	public GuiItemTranslator(ContainerItemTranslator container) {
		super(container);
	}

	@Override
	protected ResourceLocation provideTexture() {
		return new ResourceLocation("cameracraft", "textures/gui/oreDictionary.png");
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		TileItemMutator tile = container.inventory();
		fontRenderer.drawSplitString("foo\nbar", 100, 10, 50, 0x000000);
		fontRenderer.drawString("" + container.inventory().getTransmuteTime(), 30, 10, 0x000000);
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		
	}

}
