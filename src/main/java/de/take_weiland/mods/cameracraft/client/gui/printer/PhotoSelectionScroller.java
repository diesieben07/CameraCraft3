package de.take_weiland.mods.cameracraft.client.gui.printer;

import de.take_weiland.mods.cameracraft.client.PhotoDataCache;
import net.minecraft.util.MathHelper;
import de.take_weiland.mods.cameracraft.gui.ContainerPrinter;
import de.take_weiland.mods.cameracraft.gui.ContainerPrinter.ClientNodeInfo;
import de.take_weiland.mods.commons.client.Rendering;
import de.take_weiland.mods.commons.client.ScrollPane;
import de.take_weiland.mods.commons.util.JavaUtils;
import static org.lwjgl.opengl.GL11.*;

public class PhotoSelectionScroller extends ScrollPane {

	int lastMouseX;
	int lastMouseY;
	private final GuiPrinter gui;
	
	public PhotoSelectionScroller(GuiPrinter gui, int x, int y, int width, int height, int contentHeight) {
		super(gui, x, y, width, height, contentHeight);
		this.gui = gui;
	}

	@Override
	protected void drawImpl(int mouseX, int mouseY) {
		lastMouseX = mouseX;
		lastMouseY = mouseY;
		ContainerPrinter container = gui.getContainer();
		ClientNodeInfo selected = JavaUtils.get(container.getNodes(), container.getSelectedNodeIdx());
		if (selected != null) {
			String[] selectedIds = selected.photoIds;
			if (selectedIds.length != 0) {
				for (int i = 0; i < selectedIds.length; ++i) {
					Rendering.drawColoredRect(0, 10 * i, mc.fontRenderer.getStringWidth(selectedIds[i]), mc.fontRenderer.FONT_HEIGHT, 0xffddddff);
					int count = selected.counts[i];
					String txt = selectedIds[i] + (count != 0 ? " x" + Integer.toString(count) : "");
					mc.fontRenderer.drawString(txt, 0, 10 * i, 0x000000);
				}
			} else {
				mc.fontRenderer.drawString("No Photos", 0, 0, 0x000000);
			}
		}
	}

	int hoveredNode() {
		return hoveredNode(lastMouseX, lastMouseY);
	}

	private int hoveredNode(int mouseX, int mouseY) {
		if (mouseX >= 0 && mouseX <= width - scrollbarWidth - 2) {
			int selectedIdx = MathHelper.floor_float(mouseY / 10f);
			ClientNodeInfo node = gui.getContainer().getSelectedNode();
			if (node != null && JavaUtils.indexExists(node.photoIds, selectedIdx)) {
				return selectedIdx;
			}
		}
		return -1;
	}

	@Override
	protected void drawImpl() {
		// TODO: fix SevenCommons
	}

	@Override
	protected void handleMouseClick(int relX, int relY, int btn) {
		int selectedIdx = hoveredNode(relX, relY);
		if (selectedIdx >= 0) {
			ClientNodeInfo node = gui.getContainer().getSelectedNode();
			mc.sndManager.playSoundFX("random.click", 1, 1);
			node.counts[selectedIdx] += btn == 0 ? 1 : (btn == 1 && node.counts[selectedIdx] > 0) ? -1 : 0;
		}
	}

}
