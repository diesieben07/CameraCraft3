package de.take_weiland.mods.cameracraft.client.gui;

import net.minecraft.util.ResourceLocation;
import de.take_weiland.mods.cameracraft.gui.ContainerCardReader;
import de.take_weiland.mods.cameracraft.tileentity.TileCardReader;
import de.take_weiland.mods.commons.client.AbstractGuiContainer;

public class GuiCardReader extends AbstractGuiContainer<TileCardReader, ContainerCardReader> {

	private static final int YELLOW = 0;
	private static final int RED = 7;
	private static final int GREEN = 14;
	
	public GuiCardReader(ContainerCardReader container) {
		super(container);
	}

	@Override
	protected ResourceLocation provideTexture() {
		return new ResourceLocation("cameracraft:textures/gui/cardReader.png");
	}
	
	private int lampTexU;

	@Override
	public void updateScreen() {
		super.updateScreen();
		switch (container.getAccessState()) {
		case TileCardReader.NO_ACC:
		default:
			lampTexU = GREEN;
			break;
		case TileCardReader.READ_ACC:
			lampTexU = YELLOW;
			break;
		case TileCardReader.WRITE_ACC:
			lampTexU = RED;
			break;
		}
		
		if (lampTexU != GREEN && mc.theWorld.rand.nextInt(3) == 0) {
			lampTexU = GREEN;
		}
	}

	int ticks = 0;
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		
		drawTexturedModalRect(guiLeft + 40, guiTop + 20, lampTexU, 166, 7, 7);
	}

}
