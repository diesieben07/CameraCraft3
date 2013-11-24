package de.take_weiland.mods.cameracraft.client.gui;

import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import de.take_weiland.mods.cameracraft.api.PhotoStorageProvider;
import de.take_weiland.mods.cameracraft.gui.ContainerPrinter;
import de.take_weiland.mods.cameracraft.tileentity.TilePrinter;
import de.take_weiland.mods.commons.client.AbstractGuiContainer;
import de.take_weiland.mods.commons.client.ScrollPane;
import de.take_weiland.mods.commons.util.JavaUtils;

public class GuiPrinter extends AbstractGuiContainer<TilePrinter, ContainerPrinter> {

	public GuiPrinter(ContainerPrinter container) {
		super(container);
	}

	@Override
	protected ResourceLocation provideTexture() {
		return new ResourceLocation("cameracraft:textures/gui/printer.png");
	}

	private ScrollPane scroller;
	
	@Override
	public void initGui() {
		ySize = 200;
		super.initGui();
		rebuildScrollList();
	}

	private void rebuildScrollList() {
		scroller = new ScrollPane(this, guiLeft + 8, guiTop + 15, xSize - 16, 70, 0) {
			
			@Override
			protected void drawImpl() {
				drawRect(0, 0, guiLeft + 8, xSize - 16, 0x11000000);
				List<String> nodes = JavaUtils.nullToEmpty(container.getNodeNames());
				for (int i = 0; i < nodes.size(); ++i) {
					fontRenderer.drawString(nodes.get(i), 0, i * 10, 0xffffff);
				}
			}
		};
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		scroller.setContentHeight(container.getNodeNames() == null ? 0 : container.getNodeNames().size() * 10);
		scroller.draw(mouseX, mouseY);
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
		System.out.println(button.id);
	}
	
	

}
