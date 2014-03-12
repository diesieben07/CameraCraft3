package de.take_weiland.mods.cameracraft.client;

import com.google.common.collect.Queues;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.cameracraft.photo.PhotoManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;

import java.util.EnumSet;
import java.util.Queue;

public class RenderTickHandler implements ITickHandler {

	private final Minecraft mc;
	
	private final Queue<Integer> photoQueue = Queues.newArrayDeque();
	
	public RenderTickHandler(Minecraft mc) {
		this.mc = mc;
	}
	
	public void schedulePhoto(int transferId) {
		photoQueue.offer(Integer.valueOf(transferId));
	}
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		if (!photoQueue.isEmpty()) {
			int transferId = photoQueue.poll().intValue();
			GameSettings gs = mc.gameSettings;
			boolean hideGuiState = gs.hideGUI;
			int thirdPersonState = gs.thirdPersonView;
			
			int heightState = mc.displayHeight;
			int widthState = mc.displayWidth;
			
			gs.hideGUI = true;
			gs.thirdPersonView = 0;
			mc.displayHeight = mc.displayWidth = PhotoManager.PHOTO_SIZE;
			
			mc.entityRenderer.renderWorld(0, 0);
			
			byte[] rawImage = ClientUtil.rawScreenshot(mc);
			CameraCraft.executor.execute(new ScreenshotPostProcess(transferId, mc.displayWidth, mc.displayHeight, rawImage));
			
			gs.hideGUI = hideGuiState;
			gs.thirdPersonView = thirdPersonState;
			mc.displayHeight = heightState;
			mc.displayWidth = widthState;
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
