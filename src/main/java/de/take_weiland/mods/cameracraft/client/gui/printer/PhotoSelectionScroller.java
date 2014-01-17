package de.take_weiland.mods.cameracraft.client.gui.printer;

import net.minecraft.util.MathHelper;
import de.take_weiland.mods.cameracraft.gui.ContainerPrinter;
import de.take_weiland.mods.cameracraft.gui.ContainerPrinter.ClientNodeInfo;
import de.take_weiland.mods.commons.client.Rendering;
import de.take_weiland.mods.commons.client.ScrollPane;
import de.take_weiland.mods.commons.util.JavaUtils;

public class PhotoSelectionScroller extends ScrollPane {

	private final GuiPrinter gui;
	
	public PhotoSelectionScroller(GuiPrinter gui, int x, int y, int width, int height, int contentHeight) {
		super(gui, x, y, width, height, contentHeight);
		this.gui = gui;
	}

	@Override
	protected void drawImpl() {
		ContainerPrinter container = gui.getContainer();
		ClientNodeInfo selected = JavaUtils.safeListAccess(container.getNodes(), container.getSelectedNodeIdx());
		if (selected != null) {
			String[] selectedIds = selected.photoIds;
			if (selectedIds.length != 0) {
				int selectedId = container.getSelectedPhotoIdIdx();
				for (int i = 0; i < selectedIds.length; ++i) {
					Rendering.drawColoredRect(0, 10 * i, mc.fontRenderer.getStringWidth(selectedIds[i]), mc.fontRenderer.FONT_HEIGHT, 0xffddddff);
					mc.fontRenderer.drawString(selectedIds[i], 0, 10 * i, i == selectedId ? 0x7777ff : 0x000000);
				}
			} else {
				mc.fontRenderer.drawString("No Photos", 0, 0, 0x000000);
			}
		}
	}

	@Override
	protected void handleMouseClick(int relX, int relY, int btn) {
		if (relX >= 0 && relX <= width - scrollbarWidth - 2) {
			int newSelection = MathHelper.floor_float(relY / 10f);
			
			ClientNodeInfo node = gui.getContainer().getSelectedNode();
			
			if (node != null && JavaUtils.arrayIndexExists(node.photoIds, newSelection)) {
				mc.sndManager.playSoundFX("random.click", 1, 1);
				gui.getContainer().selectPhotoId(newSelection);
			}
		}
	}

}
