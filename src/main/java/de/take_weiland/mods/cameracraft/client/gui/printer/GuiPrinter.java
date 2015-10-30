package de.take_weiland.mods.cameracraft.client.gui.printer;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.collect.FluentIterable;
import com.google.common.primitives.Ints;
import de.take_weiland.mods.cameracraft.api.photo.PhotoStorage;
import de.take_weiland.mods.cameracraft.client.PhotoDataCache;
import de.take_weiland.mods.cameracraft.gui.ContainerPrinter;
import de.take_weiland.mods.cameracraft.network.PacketRequestPrintJob;
import de.take_weiland.mods.cameracraft.tileentity.TilePrinter;
import de.take_weiland.mods.commons.client.AbstractGuiContainer;
import de.take_weiland.mods.commons.client.GuiButtonImage;
import de.take_weiland.mods.commons.client.Guis;
import de.take_weiland.mods.commons.client.Rendering;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import static org.lwjgl.opengl.GL11.glColor3f;

public class GuiPrinter extends AbstractGuiContainer<ContainerPrinter> {

	public static final int BUTTON_PRINT = 0;
    private static final int BUTTON_NEXT = 1;
    private static final int BUTTON_PREV = 2;
    public static final int LIST_SIZE = 4;

    private GuiButton buttonPrint;
    private GuiButton buttonNext;
    private GuiButton buttonPrev;
    private GuiTextField textFieldAmount;

    private ItemStack currentStorageStack;
    private PhotoStorage currentStorage;
    private Iterable<Long> currentIDs;

    private int listOffset = 0;
    private Long selectedID;
    private Long hoveredID;
	
	public GuiPrinter(ContainerPrinter container) {
		super(container);
        ySize = 200;
	}

	@Override
	protected ResourceLocation provideTexture() {
		return new ResourceLocation("cameracraft:textures/gui/printer.png");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		super.initGui();

        buttonList.add(buttonPrint = new GuiButtonImage(GuiPrinter.BUTTON_PRINT, 0, 40, 20, 20, new ResourceLocation("cameracraft:textures/gui/controls.png"), 44, 0));
        buttonList.add(buttonPrev = new GuiButton(GuiPrinter.BUTTON_PREV, guiLeft + 10, guiTop + 60, 10, 20, "<"));
        buttonList.add(buttonNext = new GuiButton(GuiPrinter.BUTTON_NEXT, guiLeft + 50, guiTop + 60, 10, 20, ">"));

        textFieldAmount = Guis.addTextField(this, new GuiTextField(fontRendererObj, guiLeft + 22, guiTop + 62, 26, 16));
        textFieldAmount.setMaxStringLength(2);

        updateButtonState();
	}

    private void updateButtonState() {
        if (currentStorage == null) {
            buttonPrev.enabled = buttonNext.enabled = false;

            currentIDs = null;
            selectedID = null;
            hoveredID = null;
        } else {
            buttonPrev.enabled = listOffset != 0;
            buttonNext.enabled = currentStorage.size() > listOffset + LIST_SIZE;

            currentIDs = FluentIterable.from(currentStorage).skip(listOffset).limit(LIST_SIZE);
        }
        updatePrintBtnState();
    }

    private void updatePrintBtnState() {
        buttonPrint.enabled = currentStorage != null && selectedID != null && !Strings.isNullOrEmpty(textFieldAmount.getText());
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        super.keyTyped(typedChar, keyCode);
        if (textFieldAmount.isFocused()) {
            updatePrintBtnState();
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        ItemStack stackNow = container.inventory().getStackInSlot(TilePrinter.SLOT_STORAGE);
        if (stackNow != currentStorageStack) {
            currentStorageStack = stackNow;
            currentStorage = container.inventory().getStorage();
            updateButtonState();
        }
    }

    @Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);

        RenderHelper.disableStandardItemLighting();

        int y = guiTop + 10;

        hoveredID = null;

        if (currentIDs != null) {
            int idx = listOffset + 1;
            for (Long id : currentIDs) {
                String name = String.format("DCIM_%04d", idx);
                idx++;

                int x = guiLeft + 10;
                int width = fontRendererObj.getStringWidth(name);
                int height = fontRendererObj.FONT_HEIGHT;

                int hovX = x - 1;
                int hovY = y - 1;
                int hovWidth = width + 1;
                int hovHeight = height;

                boolean selected = id.equals(selectedID);
                boolean hover = Guis.isPointInRegion(hovX, hovY, hovWidth, hovHeight, mouseX, mouseY);
                if (hover || selected) {
                    Rendering.drawColoredQuad(hovX, hovY, hovWidth, hovHeight, 0xf0f0f0);
                }

                if (hover) {
                    hoveredID = id;
                }

                int color = selected ? 0xaa3333 : hover ? 0x000000 : 0xffffff;
                fontRendererObj.drawString(name, x, y, color);
                y += height + 3;
            }
        }

        if (selectedID != null) {
            glColor3f(1, 1, 1);
            PhotoDataCache.bindTexture(selectedID);
            Rendering.drawTexturedQuadFit(guiLeft + xSize - 70, guiTop + 10, 60, 60);
        }

        if (container.printProgress > 0) {
            zLevel = -2;
            Rendering.drawColoredQuad(guiLeft + 5, guiTop + 85, container.printProgress, 2, 0xff0000);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && hoveredID != null) {
            selectedID = hoveredID;
            mc.getSoundHandler().playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("gui.button.press"), 1));
            updatePrintBtnState();
        } else {
            super.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
	protected void actionPerformed(GuiButton button) {
		super.actionPerformed(button);
		switch (button.id) {
            case BUTTON_PRINT:
                if (selectedID != null) {
                    int amount = Objects.firstNonNull(Ints.tryParse(textFieldAmount.getText()), 0);
                    new PacketRequestPrintJob(container.windowId, selectedID, amount).sendToServer();
                }
                break;
            case BUTTON_NEXT:
                listOffset += LIST_SIZE;
                updateButtonState();
                break;
            case BUTTON_PREV:
                listOffset = Math.max(listOffset - LIST_SIZE, 0);
                updateButtonState();
                break;
		}
	}


}
