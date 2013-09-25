package de.take_weiland.mods.cameracraft.client;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import de.take_weiland.mods.cameracraft.CCUtil;
import de.take_weiland.mods.cameracraft.CameraCraft;

public class RenderTickHandler implements ITickHandler {

	private final Minecraft mc;
	
	private boolean isPhotoScheduled;
	
	public RenderTickHandler(Minecraft mc) {
		this.mc = mc;
	}
	
	public void schedulePhoto() {
		isPhotoScheduled = true;
	}
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		if (isPhotoScheduled) {
			GameSettings gs = mc.gameSettings;
			boolean hideGuiState = gs.hideGUI;
			int thirdPersonState = gs.thirdPersonView;
			
			int heightState = mc.displayHeight;
			int widthState = mc.displayWidth;
			
			gs.hideGUI = true;
			gs.thirdPersonView = 0;
			mc.displayHeight = mc.displayWidth = CCUtil.PHOTO_SIZE;
			
			mc.entityRenderer.renderWorld(0, 0);
			
			byte[] rawImage = ClientUtil.rawScreenshot(mc);
			ScreenshotPostProcess postProcess = new ScreenshotPostProcess(mc.displayWidth, mc.displayHeight, rawImage);
			CameraCraft.executor.execute(postProcess);
			
			gs.hideGUI = hideGuiState;
			gs.thirdPersonView = thirdPersonState;
			mc.displayHeight = heightState;
			mc.displayWidth = widthState;
			
			isPhotoScheduled = false;
		}
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) { }

	private static final EnumSet<TickType> TICKS = EnumSet.of(TickType.RENDER);
	
	@Override
	public EnumSet<TickType> ticks() {
		return TICKS;
	}

	@Override
	public String getLabel() {
		return "cameracraft.rendertick";
	}

}
