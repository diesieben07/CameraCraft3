package de.take_weiland.mods.cameracraft.client.gui;

import static org.lwjgl.opengl.GL11.glColor3f;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import org.lwjgl.input.Keyboard;

import de.take_weiland.mods.cameracraft.client.PhotoDataCache;
import de.take_weiland.mods.commons.client.Guis;
import de.take_weiland.mods.commons.client.Rendering;

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
		int size = Math.min(height, width) - 4; // Math.min(PHOTO_SIZE, height);
		
		int x = (width - size) / 2;
		int y = (height - size) / 2;
		
		drawRect(x, y, x + size, y + size, 0xffffff00);
		
		PhotoDataCache.bindTexture(photoId);
		glColor3f(1, 1, 1);
		Rendering.drawTexturedQuadFit(x + 2, y + 2, size - 4, size - 4);
		
		drawCenteredString(fontRenderer, photoId, width / 2, height - 14, 0xffff00);
		
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

	@Override
	protected void keyTyped(char c, int keyCode) {
		if (keyCode == Keyboard.KEY_E) {
			Guis.close();
		} else {
			super.keyTyped(c, keyCode);
		}
	}

}
