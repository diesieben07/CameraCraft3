package de.take_weiland.mods.cameracraft.client.gui.printer;

import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_SCISSOR_TEST;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glScissor;
import static org.lwjgl.opengl.GL11.glTranslatef;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;

import de.take_weiland.mods.cameracraft.api.printer.PrintJob;
import de.take_weiland.mods.cameracraft.gui.ContainerPrinter;
import de.take_weiland.mods.cameracraft.gui.ContainerPrinter.ClientNodeInfo;
import de.take_weiland.mods.cameracraft.network.PacketPrintJob;
import de.take_weiland.mods.cameracraft.tileentity.TilePrinter;
import de.take_weiland.mods.commons.client.AbstractGuiContainer;
import de.take_weiland.mods.commons.client.Guis;
import de.take_weiland.mods.commons.client.ScrollPane;
import de.take_weiland.mods.commons.util.JavaUtils;

public class GuiPrinter extends AbstractGuiContainer<TilePrinter, ContainerPrinter> {

	private static final int BUTTON_OPEN_SCROLLER = 0;
	public static final int BUTTON_PRINT = 1;
	
	private static boolean isScrollerOpen = false;
	int selectedNode = -1;
	
	int sliderToggleDelay = -1;
	int scrollerOffsetX = isScrollerOpen ? 0 : getScrollerHiddenOffset();
	private int scrollerMotion = 0;
	
	private ScrollPane networkScroller;
	private ScrollPane photoIdScroller;
	
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
		networkScroller = new NetworkListScroller(this, guiLeft + 8, guiTop + 15, xSize - 16, 70, 0);
		photoIdScroller = new PhotoSelectionScroller(this, guiLeft + 8, guiTop + 15, xSize - 16, 70, 0);
		photoIdScroller.setClip(true);
		
		buttonList.add(new ButtonOpenScroller(BUTTON_OPEN_SCROLLER, guiLeft + 4, guiTop + 15 + 30));
		buttonList.add(new GuiButton(GuiPrinter.BUTTON_PRINT, 0, 40, "Print!"));
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		glDisable(GL_LIGHTING);
		
		ClientNodeInfo[] nodes = container.getNodes();
		networkScroller.setContentHeight(nodes == null ? 0 : nodes.length * 10);
		
		if (scrollerOffsetX != 0) {
			if (scrollerOffsetX != getScrollerHiddenOffset()) {
				networkScroller.setClip(false);
				int scale = Guis.computeGuiScale();
				glPushMatrix();
				glEnable(GL_SCISSOR_TEST);
				glScissor((guiLeft + 8) * scale, mc.displayHeight - (guiTop + 15 + 70) * scale, (xSize - 16 + guiLeft + 8) * scale, 70 * scale);
				glTranslatef(scrollerOffsetX + (partialTicks * 40 * scrollerMotion), 0, 0);
				
				networkScroller.draw(mouseX, mouseY);
				glDisable(GL_SCISSOR_TEST);
				glPopMatrix();
			}
		} else {
			networkScroller.setClip(true);
			networkScroller.draw(mouseX, mouseY);
		}
		
		if (shouldDrawIds()) {
			photoIdScroller.setContentHeight(nodes[selectedNode].photoIds.length * 10);
			photoIdScroller.draw(mouseX, mouseY);
		}
		
		glEnable(GL_LIGHTING);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
//		ClientNodeInfo[] nodes = container.getNodes();
//		if (scrollerOffsetX == getScrollerHiddenOffset() && selectedNode != -1 && nodes != null && JavaUtils.arrayIndexExists(nodes, selectedNode)) {
//			String[] selectedIds = nodes[selectedNode].photoIds;
//			if (selectedIds.length != 0) {
//				for (int i = 0; i < selectedIds.length; ++i) {
//					fontRenderer.drawString(selectedIds[i], 20, 20 + 10 * i, 0x000000);
//				}
//			} else {
//				fontRenderer.drawString("No Photos", 20, 20, 0x000000);
//			}
//		}
	}
	
	private boolean shouldDrawIds() {
		ClientNodeInfo[] nodes = container.getNodes();
		return scrollerOffsetX == getScrollerHiddenOffset() && selectedNode != -1 && nodes != null && JavaUtils.arrayIndexExists(nodes, selectedNode);
	}

	@Override
	public void handleMouseInput() {
		super.handleMouseInput();
		int dWheel = Mouse.getEventDWheel();
		networkScroller.onMouseWheel(dWheel);
		photoIdScroller.onMouseWheel(dWheel);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int btn) {
		super.mouseClicked(mouseX, mouseY, btn);
		if (isScrollerOpen) {
			networkScroller.onMouseClick(mouseX, mouseY, btn);
		} else if (shouldDrawIds()) {
			photoIdScroller.onMouseClick(mouseX, mouseY, btn);
		}
	}

	@Override
	protected void mouseMovedOrUp(int mouseX, int mouseY, int btn) {
		super.mouseMovedOrUp(mouseX, mouseY, btn);
		if (isScrollerOpen) {
			networkScroller.onMouseBtnReleased(btn);
		} else if (shouldDrawIds()) {
			photoIdScroller.onMouseBtnReleased(btn);
		}
	}

	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int btn, long time) {
		super.mouseClickMove(mouseX, mouseY, btn, time);
		if (isScrollerOpen) {
			networkScroller.onMouseMoved(mouseX, mouseY);
		} else if (shouldDrawIds()) {
			photoIdScroller.onMouseMoved(mouseX, mouseY);
		}
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
			ClientNodeInfo[] nodes = container.getNodes();
			if (nodes != null && JavaUtils.arrayIndexExists(nodes, selectedNode)) {
				if (nodes[selectedNode].photoIds.length >= 1) {
					new PacketPrintJob(container, new PrintJob(nodes[selectedNode].photoIds[0])).sendToServer();
				}
			}
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
