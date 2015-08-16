package de.take_weiland.mods.cameracraft.client;

import com.google.common.collect.Queues;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.cameracraft.photo.PhotoManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;

import java.util.Queue;

public class PhotoTicker {

	private final Queue<Integer> photoQueue = Queues.newArrayDeque();
	
	public void schedulePhoto(int transferId) {
		photoQueue.offer(transferId);
	}

    @SubscribeEvent
	public void renderTick(TickEvent.RenderTickEvent event) {
        if (event.phase != TickEvent.Phase.START) {
            return;
        }

        if (!photoQueue.isEmpty()) {
            Minecraft mc = Minecraft.getMinecraft();
            int transferId = photoQueue.poll();
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
	
}
