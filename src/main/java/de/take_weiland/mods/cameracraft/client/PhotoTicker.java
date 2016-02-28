package de.take_weiland.mods.cameracraft.client;

import com.google.common.collect.Queues;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import de.take_weiland.mods.cameracraft.network.PacketImageResponse;
import de.take_weiland.mods.commons.util.Async;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;

import java.util.Queue;
import java.util.concurrent.CompletableFuture;

public class PhotoTicker {

    public static final int                                     PHOTO_SIZE = 256;
    private final Queue<CompletableFuture<PacketImageResponse>> photoQueue = Queues.newArrayDeque();
	
	public void schedulePhoto(CompletableFuture<PacketImageResponse> future) {
		photoQueue.offer(future);
	}

    @SubscribeEvent
	public void renderTick(TickEvent.RenderTickEvent event) {
        if (event.phase != TickEvent.Phase.START) {
            return;
        }

        if (!photoQueue.isEmpty()) {
            Minecraft mc = Minecraft.getMinecraft();
            CompletableFuture<PacketImageResponse> future = photoQueue.poll();
            GameSettings gs = mc.gameSettings;
            boolean hideGuiState = gs.hideGUI;
            int thirdPersonState = gs.thirdPersonView;

            int heightState = mc.displayHeight;
            int widthState = mc.displayWidth;

            gs.hideGUI = true;
            gs.thirdPersonView = 0;
            mc.displayHeight = mc.displayWidth = PHOTO_SIZE;

            mc.entityRenderer.renderWorld(0, 0);

            byte[] rawImage = ClientUtil.rawScreenshot(mc);
            Async.commonExecutor().execute(new ScreenshotPostProcess(future, mc.displayWidth, mc.displayHeight, rawImage));

            gs.hideGUI = hideGuiState;
            gs.thirdPersonView = thirdPersonState;
            mc.displayHeight = heightState;
            mc.displayWidth = widthState;
        }
    }
	
}
