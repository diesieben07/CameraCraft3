package de.take_weiland.mods.cameracraft.client.gui;

import org.lwjgl.input.Keyboard;

import com.google.common.base.Strings;

import de.take_weiland.mods.cameracraft.client.ScreenshotPostProcess;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;

public class GuiPhotoName extends GuiScreen {

	private static final int BUTTON_DONE = 0;
	
	private final ScreenshotPostProcess screenshotProccessor;	

	private GuiTextField nameField;
	private GuiButton buttonDone;
	
	public GuiPhotoName(ScreenshotPostProcess screenshotProccessor) {
		this.screenshotProccessor = screenshotProccessor;
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
		done(nameField.getText().trim());
	}

	private void done(String name) {
		screenshotProccessor.setPhotoName(Strings.emptyToNull(name));
		mc.displayGuiScreen(null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		buttonList.add((buttonDone = new GuiButton(BUTTON_DONE, width / 2 - 50, height - 30, 100, 20, I18n.getString("gui.done"))));
		nameField = new GuiTextField(fontRenderer, width / 2 - 100, 50, 200, 20);
		updateButtonState();
	}

	@Override
	protected void keyTyped(char c, int keyCode) {
		if (keyCode == Keyboard.KEY_ESCAPE) {
			done(null);
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
