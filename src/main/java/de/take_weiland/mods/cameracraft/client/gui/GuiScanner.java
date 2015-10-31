package de.take_weiland.mods.cameracraft.client.gui;

import de.take_weiland.mods.cameracraft.gui.ContainerScanner;
import de.take_weiland.mods.commons.client.AbstractGuiContainer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

public class GuiScanner extends AbstractGuiContainer<ContainerScanner> {

    private static final int BUTTON_SCAN = 0;

	public GuiScanner(ContainerScanner container) {
		super(container);
	}

	@SuppressWarnings("unchecked")
    @Override
	public void initGui() {
        super.initGui();
		buttonList.add(new GuiButton(BUTTON_SCAN, 0, 0, "Scan"));
	}

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case BUTTON_SCAN:
                container.triggerButton(0);
                break;
        }
    }

    @Override
	protected ResourceLocation provideTexture() {
		return new ResourceLocation("cameracraft:textures/gui/scanner.png");
	}

}
