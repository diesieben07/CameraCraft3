package de.take_weiland.mods.cameracraft.client.gui;

import static org.lwjgl.opengl.GL11.glColor3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;

import org.lwjgl.input.Keyboard;

import de.take_weiland.mods.cameracraft.client.EnvironmentClient;
import de.take_weiland.mods.cameracraft.client.PhotoDataCache;
import de.take_weiland.mods.cameracraft.item.CCItem;
import de.take_weiland.mods.cameracraft.network.PacketPhotoName;
import de.take_weiland.mods.commons.client.Guis;
import de.take_weiland.mods.commons.client.Rendering;

public class GuiViewPhoto extends GuiScreen {

	private static final int BUTTON_RENAME = 0;
	private static final int BUTTON_DONE = 1;
	
	private final String photoId;
	private boolean isNameable;
	private String displayName;
	
	private boolean isRenaming = false;
	
	private GuiTextField nameTextField;
	private GuiButton buttonDone;
	private GuiButton buttonRename;
	
	public GuiViewPhoto(String photoId, String displayName, boolean isNameable) {
		this.photoId = photoId;
		this.displayName = displayName;
		this.isNameable = isNameable;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		super.initGui();
		
		int photoDim = computePhotoDim();
		
		buttonRename = new GuiButtonRename(BUTTON_RENAME, (width + photoDim) / 2 + 10, height - 30);
		buttonRename.enabled = isNameable;
		buttonList.add(buttonRename);
		
		buttonDone = new GuiButtonDone(BUTTON_DONE, (width + photoDim) / 2 + 10, height - 30);
		buttonDone.drawButton = isRenaming;
		buttonList.add(buttonDone);
		
		GuiTextField oldTB = nameTextField;
		int textFieldWidth = photoDim - 20;
		nameTextField = new GuiTextField(fontRenderer, (width - textFieldWidth) / 2, height - 30, textFieldWidth, 20);
		nameTextField.setCanLoseFocus(false);
		if (oldTB != null) {
			Guis.copyState(oldTB, nameTextField);
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTick) {
		drawDefaultBackground();
		int size = computePhotoDim();
		
		int x = (width - size) / 2;
		int y = (height - size) / 2;
		
		drawRect(x, y, x + size, y + size, 0xffffff00);
		
		PhotoDataCache.bindTexture(photoId);
		glColor3f(1, 1, 1);
		Rendering.drawTexturedQuadFit(x + 2, y + 2, size - 4, size - 4);
		
		if (isRenaming) {
			nameTextField.drawTextBox();
		} else {
			String name = isNameable ? I18n.getString(CCItem.photo.getUnlocalizedName() + ".name") : displayName;
			drawCenteredString(fontRenderer, name, width / 2, height - 14, 0xffff00);
		}
		
		super.drawScreen(mouseX, mouseY, partialTick);
	}

	private int computePhotoDim() {
		return Math.min(height, width) - 4;
	}

	private boolean btnBlock;
	
	@Override
	protected void actionPerformed(GuiButton button) {
		super.actionPerformed(button);
		switch (button.id) {
		case BUTTON_DONE:
		case BUTTON_RENAME:
			if (!btnBlock) {
				toggleRename();
				btnBlock = true; // need to block the buttons because they are right on top of each other so the other one would trigger again immediately
			}
			break;
		}
	}

	private void toggleRename() {
		if (isNameable) {
			if (isRenaming) {
				displayName = nameTextField.getText();
				isNameable = false;
				new PacketPhotoName(displayName).sendToServer();
			} else {
				nameTextField.setFocused(true);
			}
			buttonDone.drawButton = isRenaming = !isRenaming;
			buttonRename.drawButton = !buttonDone.drawButton;
			buttonRename.enabled = isNameable;
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	

	@Override
	public void updateScreen() {
		super.updateScreen();
		nameTextField.updateCursorCounter();
		btnBlock = false;
	}

	@Override
	protected void keyTyped(char c, int keyCode) {
		if (isRenaming && nameTextField.isFocused()) {
			if (keyCode == Keyboard.KEY_RETURN) {
				toggleRename();
			} else {
				nameTextField.textboxKeyTyped(c, keyCode);
			}
		} else if (keyCode == Keyboard.KEY_E || keyCode == Keyboard.KEY_ESCAPE) {
			Guis.close();
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int button) {
		super.mouseClicked(mouseX, mouseY, button);
		if (isRenaming) {
			nameTextField.mouseClicked(mouseX, mouseY, button);
		}
	}

	private static final class GuiButtonRename extends GuiButton {

		public GuiButtonRename(int id, int x, int y) {
			super(id, x, y, 20, 20, "");
		}

		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY) {
			super.drawButton(mc, mouseX, mouseY);
			if (drawButton) {
				Item item = Item.nameTag;
				mc.renderEngine.bindTexture(mc.renderEngine.getResourceLocation(item.getSpriteNumber()));
				drawTexturedModelRectFromIcon(xPosition + 2, yPosition + 2, item.getIconFromDamage(0), 16, 16);
			}
		}
		
	}
	
	private static final class GuiButtonDone extends GuiButton {

		public GuiButtonDone(int id, int x, int y) {
			super(id, x, y, 20, 20, "");
		}
		
		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY) {
			super.drawButton(mc, mouseX, mouseY);
			if (drawButton) {
				mc.renderEngine.bindTexture(EnvironmentClient.CONTROLS);
				Rendering.drawTexturedQuad(xPosition + 3, yPosition + 5, 14, 10, 0, 0, 14, 10, 64);
			}
		}
		
	}
	
}
