package de.take_weiland.mods.cameracraft.client;

import java.awt.image.BufferedImage;

import net.minecraft.client.Minecraft;
import cpw.mods.fml.client.registry.RenderingRegistry;
import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.cameracraft.Environment;
import de.take_weiland.mods.cameracraft.blocks.CCBlock;
import de.take_weiland.mods.cameracraft.client.render.RenderBlockCable;

public class EnvironmentClient implements Environment {

	private Minecraft mc;
	
	@Override
	public void setup() {
		mc = Minecraft.getMinecraft();
		int renderId = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(renderId, new RenderBlockCable());
		CCBlock.cable.injectRenderId(renderId);
	}

	@Override
	public void executePhoto() {
		byte[] rawImage = ClientUtil.rawScreenshot(mc);
		CameraCraft.executor.execute(new ScreenshotPostProcess(mc.displayWidth, mc.displayHeight, rawImage));
	}

	@Override
	public void handleClientPhotoData(BufferedImage data) {
		// TODO Auto-generated method stub
		
	}

}
