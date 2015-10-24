package de.take_weiland.mods.cameracraft.client.gui.printer;

import de.take_weiland.mods.cameracraft.gui.ContainerPrinter;
import de.take_weiland.mods.commons.client.AbstractGuiContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import static org.lwjgl.opengl.GL11.*;

public class GuiPrinter extends AbstractGuiContainer<ContainerPrinter> {

	private static final int BUTTON_OPEN_SCROLLER = 0;
	public static final int BUTTON_PRINT = 1;
	
	private static boolean isScrollerOpen = false;
	
	int sliderToggleDelay = -1;
	int scrollerOffsetX = isScrollerOpen ? 0 : getScrollerHiddenOffset();
	private int scrollerMotion = 0;
	
	public GuiPrinter(ContainerPrinter container) {
		super(container);
	}

	int getScrollerHiddenOffset() {
		return - (xSize - 16);
	}
	
	@Override
	protected ResourceLocation provideTexture() {
		return new ResourceLocation("cameracraft:textures/gui/printer.png");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		ySize = 200;
		super.initGui();

		buttonList.add(new ButtonOpenScroller(BUTTON_OPEN_SCROLLER, guiLeft + 4, guiTop + 15 + 30));
		buttonList.add(new GuiButton(GuiPrinter.BUTTON_PRINT, 0, 40, 50, 20, "Print!"));
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		glDisable(GL_LIGHTING);

		glEnable(GL_LIGHTING);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		super.actionPerformed(button);
		switch (button.id) {
		case BUTTON_OPEN_SCROLLER:
			if (scrollerMotion == 0) {
				toggleSlider();
			}
			break;
		case BUTTON_PRINT:

		}
	}

	private void toggleSlider() {
		scrollerMotion = -Integer.signum(scrollerOffsetX + 1);
		isScrollerOpen = !isScrollerOpen;
	}

	@Override
	public void updateScreen() {
		if (sliderToggleDelay >= 0) {
			if (--sliderToggleDelay == -1) {
				toggleSlider();
			}
		}
		
		scrollerOffsetX += scrollerMotion * 40;
		if (scrollerOffsetX <= getScrollerHiddenOffset() || scrollerOffsetX >= 0) {
			scrollerOffsetX = MathHelper.clamp_int(scrollerOffsetX, getScrollerHiddenOffset(), 0);
			scrollerMotion = 0;
		}
		super.updateScreen();
	}
	
	private class ButtonOpenScroller extends GuiButton {
		
		ButtonOpenScroller(int id, int x, int y) {
			super(id, x, y, 4, 7, "");
		}

		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY) {
			GuiPrinter.this.bindTexture();
			drawTexturedModalRect(xPosition, yPosition, 176 + (scrollerOffsetX == getScrollerHiddenOffset() ? 0 : 4), 0, 4, 7);
		}
		
	}

}
