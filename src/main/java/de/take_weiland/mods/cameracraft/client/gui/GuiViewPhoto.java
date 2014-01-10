package de.take_weiland.mods.cameracraft.client.gui;

import org.lwjgl.opengl.GL11;

import de.take_weiland.mods.cameracraft.client.PhotoDataCache;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiViewPhoto extends GuiScreen {

	private final String photoId;
	
	public GuiViewPhoto(String photoId) {
		this.photoId = photoId;
	}
	
	@Override
	public void initGui() {
		super.initGui();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTick) {
		drawDefaultBackground();
		PhotoDataCache.bindTexture(photoId);
		GL11.glColor3f(1, 1, 1);
		drawTexturedModalRect(0, 0, 0, 0, 256, 256);
		
		super.drawScreen(mouseX, mouseY, partialTick);
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		super.actionPerformed(button);
		switch (button.id) {
		
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

}
