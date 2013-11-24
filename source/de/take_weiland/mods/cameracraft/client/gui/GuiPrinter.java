package de.take_weiland.mods.cameracraft.client.gui;

import java.util.List;

import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import de.take_weiland.mods.cameracraft.gui.ContainerPrinter;
import de.take_weiland.mods.cameracraft.tileentity.TilePrinter;
import de.take_weiland.mods.commons.client.AbstractGuiContainer;
import de.take_weiland.mods.commons.client.Guis;
import de.take_weiland.mods.commons.client.ScrollPane;
import de.take_weiland.mods.commons.util.JavaUtils;

public class GuiPrinter extends AbstractGuiContainer<TilePrinter, ContainerPrinter> {

	private class ButtonOpenScroller extends GuiButton {
		
		public ButtonOpenScroller(int id, int x, int y) {
			super(id, x, y, 4, 7, "");
		}

		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY) {
//			if (scrollerOffsetX == getScrollerHiddenOffset()) {
//				field_82253_i = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
//				
//			}
			GuiPrinter.this.bindTexture();
			drawTexturedModalRect(xPosition, yPosition, 176 + (scrollerOffsetX == getScrollerHiddenOffset() ? 0 : 4), 0, 4, 7);
		}
		
	}
	
	int scrollerOffsetX = getScrollerHiddenOffset();

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
	
	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		ySize = 200;
		super.initGui();
		scroller = new ScrollPane(this, guiLeft + 8, guiTop + 15, xSize - 16, 70, 0) {
			
			@SuppressWarnings("synthetic-access")
			@Override
			protected void drawImpl() {
				List<String> nodes = JavaUtils.nullToEmpty(container.getNodeNames());
				int size = nodes.size();
				drawRect(0, 0, xSize - 16, size * 10, 0x44000000);
				for (int i = 0; i < size; ++i) {
					fontRenderer.drawString(nodes.get(i), 0, i * 10, 0xffffff);
				}
			}
		};
		
		buttonList.add(new ButtonOpenScroller(0, guiLeft + 4, guiTop + 15 + 30));
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		glDisable(GL_LIGHTING);
		
		List<String> nodes = container.getNodeNames();
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

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
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
