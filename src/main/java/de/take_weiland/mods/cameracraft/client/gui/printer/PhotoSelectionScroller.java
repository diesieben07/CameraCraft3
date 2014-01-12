package de.take_weiland.mods.cameracraft.client.gui.printer;

import net.minecraft.util.MathHelper;
import de.take_weiland.mods.cameracraft.gui.ContainerPrinter.ClientNodeInfo;
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
		ClientNodeInfo[] nodes = gui.getContainer().getNodes();
		String[] selectedIds = nodes[gui.selectedNode].photoIds;
		if (selectedIds.length != 0) {
			for (int i = 0; i < selectedIds.length; ++i) {
				mc.fontRenderer.drawString(selectedIds[i], 0, 10 * i, i == gui.selectedId ? 0x7777ff : 0x000000);
			}
		} else {
			mc.fontRenderer.drawString("No Photos", 0, 0, 0x000000);
		}
	}

	@Override
	protected void handleMouseClick(int relX, int relY, int btn) {
		if (relX >= 0 && relX <= width - scrollbarWidth - 2) {
			int newSelection = MathHelper.floor_float(relY / 10f);
			
			ClientNodeInfo node = gui.getContainer().getNodes()[gui.selectedNode];
			
			if (JavaUtils.arrayIndexExists(node.photoIds, newSelection)) {
				mc.sndManager.playSoundFX("random.click", 1, 1);
				gui.selectedId = newSelection;
			}
		}
	}

}
