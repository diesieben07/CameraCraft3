package de.take_weiland.mods.cameracraft.client.gui;

import java.util.List;

import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import de.take_weiland.mods.cameracraft.gui.ContainerItemTranslator;
import de.take_weiland.mods.cameracraft.tileentity.TileItemMutator;
import de.take_weiland.mods.commons.client.AbstractGuiContainer;
import de.take_weiland.mods.commons.util.JavaUtils;
import de.take_weiland.mods.commons.util.Listenable;

public class GuiItemTranslator extends AbstractGuiContainer<TileItemMutator, ContainerItemTranslator> implements Listenable.Listener<TileItemMutator> {

	private static final RenderItem itemRenderer = new RenderItem();
	private static final Function<ItemStack, String> DISPLAY_NAME_FUNC = new Function<ItemStack, String>() {

		@Override
		public String apply(ItemStack stack) {
			return stack.getDisplayName();
		}
	};
	
	private List<ItemStack> ores = ImmutableList.of();
	private int mostWideText = 0;
	private int scrollbarY = -1;
	
	private int scrollbarClickOffset = -1;
	
	public GuiItemTranslator(ContainerItemTranslator container) {
		super(container);
		container.inventory().addListener(this);
	}

	@Override
	public void initGui() {
		xSize = 256;
		super.initGui();
	}

	@Override
	protected ResourceLocation provideTexture() {
		return new ResourceLocation("cameracraft", "textures/gui/oreDictionary.png");
	}
	
	private static final int ORE_SELECT_ITEM_HEIGHT = 18;
	private static final int ORE_SELECT_Y_START = 10;
	private static final int ORE_SELECT_X_START = 120;
	private static final int ORE_SELECT_ICON_WIDTH = 20;
	private static final int ORE_SELECT_X_TEXT = ORE_SELECT_X_START + ORE_SELECT_ICON_WIDTH;

	private static final int ORE_SELECT_ITEM_COUNT = 4;
	
	private static final int SCROLLBAR_HEIGHT = 20;
	private static final int SCROLLBAR_WIDTH = 4;
	private static final int SCROLLBAR_MAX = ORE_SELECT_ITEM_HEIGHT * ORE_SELECT_ITEM_COUNT - 4 - SCROLLBAR_HEIGHT;

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		if (scrollbarY >= 0) {
			int dWheel = -Integer.signum(Mouse.getDWheel());
			scrollbarY = MathHelper.clamp_int(scrollbarY + dWheel * (SCROLLBAR_MAX / (ores.size() - ORE_SELECT_ITEM_COUNT)), 0, SCROLLBAR_MAX);
		}
		
		
		TileItemMutator tile = container.inventory();
		
		int listOffset = getListOffsetForScrollbar();
		
		int count = Math.min(ORE_SELECT_ITEM_COUNT, ores.size() - listOffset);
		
		for (int index = 0; index < count; index++) {
			ItemStack ore = ores.get(index + listOffset);
			int y = ORE_SELECT_Y_START + index * ORE_SELECT_ITEM_HEIGHT;
			
			boolean isMouseOver = isMouseOverResult(index, mouseX, mouseY);
			boolean isSelected = tile.getSelectedResult() == index + listOffset;
			
			if (isMouseOver || isSelected) {
				drawRect(ORE_SELECT_X_TEXT - 1, y + 2, ORE_SELECT_X_TEXT + 1 + mostWideText, y + 5 + fontRenderer.FONT_HEIGHT, isSelected ? 0xff4444ff : 0xffaaaaaa);
			}
			
			fontRenderer.drawString(ore.getDisplayName(), ORE_SELECT_X_TEXT, y + 4, 0x000000);
			
			GL11.glEnable(GL11.GL_LIGHTING);
			itemRenderer.renderItemIntoGUI(fontRenderer, mc.renderEngine, ore, ORE_SELECT_X_START, y);
			GL11.glDisable(GL11.GL_LIGHTING);
		}
		
		if (scrollbarY >= 0) {
			int scrollbarX = scrollbarX();
			drawRect(scrollbarX, ORE_SELECT_Y_START + scrollbarY, scrollbarX + SCROLLBAR_WIDTH, ORE_SELECT_Y_START + scrollbarY + SCROLLBAR_HEIGHT, 0xff000000);
		}
		
		int progress = tile.scaleTransmuteProgress(22);
		if (progress >= 0) {
			bindTexture();
			GL11.glColor4f(1, 1, 1, 1);
			drawTexturedModalRect(44, 26, 0, 166, progress, 16);
		}
	}

	private int getListOffsetForScrollbar() {
		if (scrollbarY >= 0) {
			return MathHelper.floor_float(((float)(ores.size() - ORE_SELECT_ITEM_COUNT) / SCROLLBAR_MAX) * scrollbarY); 
		} else {
			return 0;
		}
	}
	
	private int scrollbarX() {
		return ORE_SELECT_X_TEXT + 3 + mostWideText;
	}
	
	private boolean isMouseOverResult(int resultIndex, int mouseX, int mouseY) {
		return scrollbarClickOffset < 0 && isPointInRegion(ORE_SELECT_X_START, ORE_SELECT_Y_START + ORE_SELECT_ITEM_HEIGHT * resultIndex, ORE_SELECT_ICON_WIDTH + mostWideText, 16, mouseX, mouseY);
	}
	
	private boolean isScrollbarClicked(int mouseX, int mouseY) {
		return scrollbarY >= 0 && isPointInRegion(scrollbarX(), ORE_SELECT_Y_START + scrollbarY, SCROLLBAR_WIDTH, SCROLLBAR_HEIGHT, mouseX, mouseY);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int button) {
		if (button == 0 && mouseX >= ORE_SELECT_X_START && mouseY >= ORE_SELECT_Y_START) {
			int size = ores.size();
			for (int index = 0; index < size; index++) {
				if (isMouseOverResult(index, mouseX, mouseY)) {
					triggerButton(index + getListOffsetForScrollbar());
					break;
				}
			}
		}
		
		if (button == 0 && isScrollbarClicked(mouseX, mouseY)) {
			scrollbarClickOffset = mouseY - scrollbarY - guiTop - ORE_SELECT_Y_START;
			System.out.println(scrollbarClickOffset);
		}
		super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int button, long timeSinceStart) {
		super.mouseClickMove(mouseX, mouseY, button, timeSinceStart);
		if (button == 0 && scrollbarClickOffset > 0) {
			scrollbarY = MathHelper.clamp_int(mouseY - guiTop - ORE_SELECT_Y_START - scrollbarClickOffset, 0, SCROLLBAR_MAX);
		}
	}

	@Override
	protected void mouseMovedOrUp(int mouseX, int mouseY, int button) {
		super.mouseMovedOrUp(mouseX, mouseY, button);
		if (button != -1) {
			scrollbarClickOffset = -1;
		}
	}

	@Override
	public void onChange(TileItemMutator inventory) {
		ores = JavaUtils.nullToEmpty(container.inventory().getTransmutingResult());
		int mostWideText = 0;
		for (String displayName : Iterables.transform(ores, DISPLAY_NAME_FUNC)) {
			int width = fontRenderer.getStringWidth(displayName);
			if (width > mostWideText) {
				mostWideText = width;
			}
		}
		this.mostWideText = mostWideText;
		int newScrollbarY = ores.size() > 4 ? 0 : -1;
		if (Integer.signum(newScrollbarY + 1) != Integer.signum(scrollbarY + 1)) {
			scrollbarY = newScrollbarY;
		}
	}

	@Override
	public void onGuiClosed() {
		container.inventory().removeListener(this);
		super.onGuiClosed();
	}
	
}
