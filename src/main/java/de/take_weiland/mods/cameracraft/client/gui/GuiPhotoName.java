package de.take_weiland.mods.cameracraft.client.gui;

import de.take_weiland.mods.commons.client.I18n;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import org.lwjgl.input.Keyboard;

import de.take_weiland.mods.commons.client.Guis;

import java.util.function.Consumer;

public class GuiPhotoName extends GuiScreen {

	private static final int BUTTON_DONE = 0;
	
	private final String oldName;
	private final Consumer<String> newNameHandler;

	private GuiTextField nameField;
	private GuiButton buttonDone;
	
	public GuiPhotoName(String oldName, Consumer<String> newNameHandler) {
		this.oldName = oldName;
		this.newNameHandler = newNameHandler;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		nameField.drawTextBox();
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		switch (button.id) {
		case BUTTON_DONE:
			done();
			break;
		}
	}
	
	private void done() {
		newNameHandler.accept(nameField.getText().trim());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		buttonList.add((buttonDone = new GuiButton(BUTTON_DONE, width / 2 - 75, height - 30, 150, 20, I18n.translate("gui.done"))));


		if (nameField != null) {
            nameField.xPosition = width / 2 - 100;
        } else {
            nameField = new GuiTextField(fontRendererObj, width / 2 - 100, 50, 200, 20);
            nameField.setFocused(true);
		}
		
		updateButtonState();
	}

	@Override
	protected void keyTyped(char c, int keyCode) {
		if (keyCode == Keyboard.KEY_ESCAPE) {
			Guis.close();
		}
		if (nameField.isFocused()) {
			nameField.textboxKeyTyped(c, keyCode);
			if (keyCode == Keyboard.KEY_RETURN && nameField.getText().length() > 0) {
				done();
			}
			updateButtonState();
		}
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int button) {
		super.mouseClicked(mouseX, mouseY, button);
		nameField.mouseClicked(mouseX, mouseY, button);
	}

	private void updateButtonState() {
		buttonDone.enabled = nameField.getText().length() > 0;
	}

	@Override
	public void updateScreen() {
		nameField.updateCursorCounter();
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
}
