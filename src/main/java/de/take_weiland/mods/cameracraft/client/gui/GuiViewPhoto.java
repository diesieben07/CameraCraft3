package de.take_weiland.mods.cameracraft.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiViewPhoto extends GuiScreen {

	private final String photoId;
	private 
	
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
		
		
		
		super.drawScreen(mouseX, mouseY, partialTick);
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		super.actionPerformed(button);
		switch (button.id) {
		
		}
	}

}
