package de.take_weiland.mods.cameracraft.client.gui.printer;

import java.util.List;

import net.minecraft.util.MathHelper;
import de.take_weiland.mods.cameracraft.gui.ContainerPrinter.ClientNodeInfo;
import de.take_weiland.mods.commons.client.Guis;
import de.take_weiland.mods.commons.client.Rendering;
import de.take_weiland.mods.commons.client.ScrollPane;
import de.take_weiland.mods.commons.util.JavaUtils;

public class NetworkListScroller extends ScrollPane {
	
	private final GuiPrinter gui;
	int overNode;

	NetworkListScroller(GuiPrinter gui, int x, int y, int width, int height, int contentHeight) {
		super(gui, x, y, width, height, contentHeight);
		this.gui = gui;
	}

	@Override
	protected void drawImpl(int mouseX, int mouseY) {
		drawRect(0, 0, width, Math.max(contentHeight, height), 0x44000000);
		
		List<ClientNodeInfo> nodes = gui.getContainer().getNodes();
		if (nodes != null) {
			int size = nodes.size();
			int selectedNode = gui.getContainer().getSelectedNodeIdx();
			int x = 0;
			int y = 0;
			overNode = -1;
			for (int i = 0; i < size; ++i) {
				Rendering.drawColoredRect(x, y, 16, 16, i == selectedNode ? 0xff7777ff : 0xff000000);
				if (Guis.isPointInRegion(x, y, 16, 16, mouseX, mouseY)) {
					overNode = i;
				}
				x += 20;
				if (x > getGridWidth()) {
					x = 0;
					y+= 20;
				}
			}
			
//			for (int i = 0; i < size; ++i) {
//				mc.fontRenderer.drawString(nodes.get(i).displayName, 1, 1 + i * 10, i == selectedNode ? 0x7777ff : 0xffffff);
//			}
		}
	}
	
	private int getGridWidth() {
		return width - 20 - scrollbarWidth;
	}
	
	private int elementsPerRow() {
		return MathHelper.ceiling_float_int(getGridWidth() / 20f);
	}
	
	private int getRowCount() {
		List<ClientNodeInfo> nodes = gui.getContainer().getNodes();
		if (nodes == null) {
			return 0;
		} else {
			return MathHelper.ceiling_float_int(nodes.size() / (float)elementsPerRow());
		}
	}
	
	void updateHeight() {
		setContentHeight(getRowCount() * 20);
	}

	@Override
	protected void handleMouseClick(int relX, int relY, int btn) {
		if (relX >= 0 && relX <= width - scrollbarWidth - 2) {
			int row = MathHelper.floor_float(relY / 20f);
			int col = MathHelper.floor_float(relX / 20f);
			
			int elemsPerRow = MathHelper.ceiling_float_int(getGridWidth() / 20f);
			int selection = row * elemsPerRow + col;
			
			List<ClientNodeInfo> nodes = gui.getContainer().getNodes();
			
			if (JavaUtils.listIndexExists(nodes, selection)) {
				mc.sndManager.playSoundFX("random.click", 1, 1);
				gui.sliderToggleDelay = 10;
				gui.getContainer().selectNode(selection);
			}
		}
	}

	@Override
	protected void drawImpl() { }
}