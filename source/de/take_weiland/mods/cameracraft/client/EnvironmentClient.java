package de.take_weiland.mods.cameracraft.client;

import cpw.mods.fml.client.registry.RenderingRegistry;
import de.take_weiland.mods.cameracraft.Environment;
import de.take_weiland.mods.cameracraft.blocks.CCBlock;
import de.take_weiland.mods.cameracraft.client.render.RenderBlockCable;

public class EnvironmentClient implements Environment {

	@Override
	public void setup() {
		int renderId = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(renderId, new RenderBlockCable());
		CCBlock.cable.injectRenderId(renderId);
	}

	@Override
	public void executePhoto() {
		System.out.println("taking photo");
	}

}
