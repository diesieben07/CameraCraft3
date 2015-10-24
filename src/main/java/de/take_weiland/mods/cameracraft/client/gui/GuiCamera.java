package de.take_weiland.mods.cameracraft.client.gui;

import de.take_weiland.mods.commons.client.SlotDrawHooks;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.inventory.Slot;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import de.take_weiland.mods.cameracraft.gui.ContainerCamera;
import de.take_weiland.mods.cameracraft.inv.InventoryCamera;
import de.take_weiland.mods.cameracraft.item.CameraType;
import de.take_weiland.mods.commons.client.AbstractGuiContainer;
import de.take_weiland.mods.commons.client.Rendering;

public class GuiCamera extends AbstractGuiContainer<ContainerCamera> implements SlotDrawHooks {

	private int lidXSize = 0;
	private int lidMovement = 0;
	private boolean wasClosed;
	
	public GuiCamera(ContainerCamera container) {
		super(container);
		wasClosed = container.inventory().isLidClosed();
		lidXSize = wasClosed ? 16 : 0;
		lidMovement = wasClosed ? 1 : -1;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		super.initGui();
		if (container.inventory().hasLid()) {
			buttonList.add(new GuiButton(ContainerCamera.BUTTON_TOGGLE_LID, 10, 10, 80, 20, "ToggleLid"));
		}
		if (container.inventory().getType() == CameraType.FILM) {
			buttonList.add(new GuiButton(ContainerCamera.BUTTON_REWIND_FILM, 10, 40, 80, 20, "Rewind"));
		}
	}

	@Override
	protected ResourceLocation provideTexture() {
		return container.inventory().getType().getGuiTexture();
	}

    @Override
    public boolean preDraw(Slot slot) {
        if (shouldDrawLid(slot)) {
            int x = slot.xDisplayPosition;
            int y = slot.yDisplayPosition;
            Rendering.drawColoredQuad(x, y, lidXSize, 16, 0xff000000, 101); // zlevel 101 to go above the itemstacks
            return lidXSize != 16;
        } else {
            return true;
        }
    }

	private boolean shouldDrawLid(Slot slot) {
		InventoryCamera inv = container.inventory();
		return (inv.isLidClosed() || lidXSize != 0) && slot.inventory == inv && slot.getHasStack() && inv.storageSlot() == slot.getSlotIndex();
	}
	
	@Override
	public void updateScreen() {
		super.updateScreen();
		boolean isClosed = container.inventory().isLidClosed();
		if (isClosed != wasClosed) {
			wasClosed = isClosed;
			lidMovement = isClosed ? 1 : -1;
		}
		lidXSize = MathHelper.clamp_int(lidXSize + lidMovement, 0, 16);
	}

}
