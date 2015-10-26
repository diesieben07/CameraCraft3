package de.take_weiland.mods.cameracraft.client.gui.printer;

import com.google.common.collect.FluentIterable;
import de.take_weiland.mods.cameracraft.api.photo.PhotoStorage;
import de.take_weiland.mods.cameracraft.gui.ContainerPrinter;
import de.take_weiland.mods.cameracraft.tileentity.TilePrinter;
import de.take_weiland.mods.commons.client.AbstractGuiContainer;
import de.take_weiland.mods.commons.client.Guis;
import de.take_weiland.mods.commons.client.Rendering;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiPrinter extends AbstractGuiContainer<ContainerPrinter> {

	public static final int BUTTON_PRINT = 0;
    private static final int BUTTON_NEXT = 1;
    private static final int BUTTON_PREV = 2;
    public static final int LIST_SIZE = 4;

    private GuiButton buttonPrint, buttonNext, buttonPrev;

    private ItemStack currentStorageStack;
    private PhotoStorage currentStorage;

    private int listOffset = 0;
	
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

        buttonList.add(buttonPrint = new GuiButton(GuiPrinter.BUTTON_PRINT, 0, 40, 50, 20, "Print!"));
        buttonList.add(buttonPrev = new GuiButton(GuiPrinter.BUTTON_PREV, guiLeft + 10, guiTop + 60, 10, 20, "<"));
        buttonList.add(buttonNext = new GuiButton(GuiPrinter.BUTTON_NEXT, guiLeft + 50, guiTop + 60, 10, 20, ">"));

        updateButtonState();
	}

    private void updateButtonState() {
        PhotoStorage storage = getStorage();
        if (storage == null) {
            buttonPrev.enabled = buttonNext.enabled = false;
        } else {
            buttonPrev.enabled = listOffset != 0;
            buttonNext.enabled = storage.size() > listOffset + LIST_SIZE;
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

        PhotoStorage storage = getStorage();
        if (storage != null) {
            for (Long id : FluentIterable.from(storage).skip(listOffset).limit(LIST_SIZE)) {
                String name = String.format("DCIM_%04d", id);

                int x = guiLeft + 10;
                int width = fontRendererObj.getStringWidth(name);
                int height = fontRendererObj.FONT_HEIGHT;

                int hovX = x - 1;
                int hovY = y - 1;
                int hovWidth = width + 1;
                int hovHeight = height;

                boolean hover = Guis.isPointInRegion(hovX, hovY, hovWidth, hovHeight, mouseX, mouseY);
                if (hover) {
                    Rendering.drawColoredQuad(hovX, hovY, hovWidth, hovHeight, 0xf0f0f0);
                }

                fontRendererObj.drawString(name, x, y, hover ? 0x000000 : 0xffffff);
                y += height + 3;
            }
        }
    }

    private PhotoStorage getStorage() {
        return currentStorage;
    }

    @Override
	protected void actionPerformed(GuiButton button) {
		super.actionPerformed(button);
		switch (button.id) {
            case BUTTON_PRINT:
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
