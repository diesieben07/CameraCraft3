package de.take_weiland.mods.cameracraft.client.gui;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import de.take_weiland.mods.cameracraft.gui.ContainerItemTranslator;
import de.take_weiland.mods.cameracraft.tileentity.TileItemMutator;
import de.take_weiland.mods.commons.client.AbstractGuiContainer;
import de.take_weiland.mods.commons.templates.AdvancedInventory;

public class GuiItemTranslator extends AbstractGuiContainer<TileItemMutator, ContainerItemTranslator> implements AdvancedInventory.Listener {

	private String oreText = "";
	
	public GuiItemTranslator(ContainerItemTranslator container) {
		super(container);
		container.inventory().registerListener(this);
	}

	@Override
	protected ResourceLocation provideTexture() {
		return new ResourceLocation("cameracraft", "textures/gui/oreDictionary.png");
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		fontRenderer.drawSplitString(oreText, 100, 10, 50, 0x000000);
		fontRenderer.drawString("" + container.inventory().getTransmuteTime(), 30, 10, 0x000000);
	}

	@Override
	public void onInventoryChanged(AdvancedInventory inventory) {
		TileItemMutator tile = container.inventory();
		ItemStack result = tile.getTransmutingResult();
		if (result != null) {
			oreText = result.getDisplayName();
		} else {
			oreText = "";
		}
	}

	@Override
	public void onGuiClosed() {
		container.inventory().removeListener(this);
		super.onGuiClosed();
	}
	
}
