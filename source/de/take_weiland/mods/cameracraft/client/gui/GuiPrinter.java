package de.take_weiland.mods.cameracraft.client.gui;

import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_SCISSOR_TEST;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glScissor;
import static org.lwjgl.opengl.GL11.glTranslatef;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Mouse;

import de.take_weiland.mods.cameracraft.gui.ContainerPrinter;
import de.take_weiland.mods.cameracraft.tileentity.TilePrinter;
import de.take_weiland.mods.commons.client.AbstractGuiContainer;
import de.take_weiland.mods.commons.client.Guis;
import de.take_weiland.mods.commons.client.ScrollPane;
import de.take_weiland.mods.commons.util.JavaUtils;

public class GuiPrinter extends AbstractGuiContainer<TilePrinter, ContainerPrinter> {

	private static boolean isScrollerOpen = false;
	
	private class ButtonOpenScroller extends GuiButton {
		
		public ButtonOpenScroller(int id, int x, int y) {
			super(id, x, y, 4, 7, "");
		}

		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY) {
			GuiPrinter.this.bindTexture();
			drawTexturedModalRect(xPosition, yPosition, 176 + (scrollerOffsetX == getScrollerHiddenOffset() ? 0 : 4), 0, 4, 7);
		}
		
	}
	
	int scrollerOffsetX = isScrollerOpen ? 0 : getScrollerHiddenOffset();

	private int scrollerMotion = 0;
	
	public GuiPrinter(ContainerPrinter container) {
		super(container);
	}

	private ScrollPane scroller;
	
	int getScrollerHiddenOffset() {
		return - (xSize - 16);
	}
	
	@Override
	protected ResourceLocation provideTexture() {
		return new ResourceLocation("cameracraft:textures/gui/printer.png");
	}
	
	int selectedNode = -1;
	
	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		ySize = 200;
		super.initGui();
		scroller = new ScrollPane(this, guiLeft + 8, guiTop + 15, xSize - 16, 70, 0) {
			
			@Override
			protected void drawImpl() {
				drawRect(0, 0, width, Math.max(contentHeight, height), 0x44000000);
				
				List<String> nodes = getNodes();
				int size = nodes.size();
				for (int i = 0; i < size; ++i) {
					mc.fontRenderer.drawString(nodes.get(i), 1, 1 + i * 10, selectedNode == i ? 0x000000 : 0xffffff);
				}
			}

			@Override
			protected void handleMouseClick(int relX, int relY, int btn) {
				if (relX >= 0 && relX <= width) {
					int newSelection = MathHelper.floor_float(relY / 10f);
					if (JavaUtils.listIndexExists(getNodes(), newSelection)) {
						selectedNode = newSelection;
					}
				}
			}
		};
		
		buttonList.add(new ButtonOpenScroller(0, guiLeft + 4, guiTop + 15 + 30));
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		glDisable(GL_LIGHTING);
		
		List<String> nodes = getNodes();
		scroller.setContentHeight(nodes == null ? 0 : nodes.size() * 10);
		
		if (scrollerOffsetX != 0) {
			scroller.setClip(false);
			int scale = Guis.computeGuiScale(mc);
			glPushMatrix();
			glEnable(GL_SCISSOR_TEST);
			glScissor((guiLeft + 8) * scale, mc.displayHeight - (guiTop + 15 + 70) * scale, (xSize - 16 + guiLeft + 8) * scale, 70 * scale);
			glTranslatef(scrollerOffsetX + (partialTicks * 40 * scrollerMotion), 0, 0);
			
			scroller.draw(mouseX, mouseY);
			glDisable(GL_SCISSOR_TEST);
			glPopMatrix();
		} else {
			scroller.setClip(true);
			scroller.draw(mouseX, mouseY);
		}
		glEnable(GL_LIGHTING);

	}

	List<String> getNodes() {
		return JavaUtils.nullToEmpty(container.getNodeNames());
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}

	@Override
	public void handleMouseInput() {
		super.handleMouseInput();
		scroller.onMouseWheel(Mouse.getEventDWheel());
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int btn) {
		super.mouseClicked(mouseX, mouseY, btn);
		scroller.onMouseClick(mouseX, mouseY, btn);
	}

	@Override
	protected void mouseMovedOrUp(int mouseX, int mouseY, int btn) {
		super.mouseMovedOrUp(mouseX, mouseY, btn);
		scroller.onMouseBtnReleased(btn);
	}

	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int btn, long time) {
		super.mouseClickMove(mouseX, mouseY, btn, time);
		scroller.onMouseMoved(mouseX, mouseY);
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		super.actionPerformed(button);
		if (button.id == 0 && scrollerMotion == 0) {
			scrollerMotion = -Integer.signum(scrollerOffsetX + 1);
			isScrollerOpen = !isScrollerOpen;
		}
	}

	@Override
	public void updateScreen() {
		scrollerOffsetX += scrollerMotion * 40;
		if (scrollerOffsetX <= getScrollerHiddenOffset() || scrollerOffsetX >= 0) {
			scrollerOffsetX = MathHelper.clamp_int(scrollerOffsetX, getScrollerHiddenOffset(), 0);
			scrollerMotion = 0;
		}
		super.updateScreen();
	}
	
	

}
