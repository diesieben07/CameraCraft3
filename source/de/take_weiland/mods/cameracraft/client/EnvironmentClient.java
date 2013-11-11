package de.take_weiland.mods.cameracraft.client;

import java.awt.image.BufferedImage;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import de.take_weiland.mods.cameracraft.CCSounds;
import de.take_weiland.mods.cameracraft.Environment;
import de.take_weiland.mods.cameracraft.blocks.CCBlock;
import de.take_weiland.mods.cameracraft.client.gui.GuiPhotoName;
import de.take_weiland.mods.cameracraft.client.render.RenderBlockCable;
import de.take_weiland.mods.commons.util.Consumer;

public class EnvironmentClient implements Environment {

	private Minecraft mc;
	private RenderTickHandler rth;
	
	@Override
	public void preInit() {
		mc = Minecraft.getMinecraft();
		
		rth = new RenderTickHandler(mc);
		TickRegistry.registerTickHandler(rth, Side.CLIENT);
		
		int renderId = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(renderId, new RenderBlockCable());
		CCBlock.cable.injectRenderId(renderId);
		
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void onPhotoRequest(int transferId) {
		rth.schedulePhoto(transferId);
	}

	@Override
	public void handleClientPhotoData(BufferedImage data) {
		// TODO Auto-generated method stub
		
	}
	
	@ForgeSubscribe
	public void onSoundLoad(SoundLoadEvent event) {
		for (CCSounds sound : CCSounds.values()) {
			sound.register(event.manager);
		}
	}

	@Override
	public void displayNamePhotoGui(String oldName) {
		mc.displayGuiScreen(new GuiPhotoName(oldName, new Consumer<String>() {
			
			@Override
			public void apply(String input) {
				// TODO Auto-generated method stub
				
			}
			
		}));
	}

}
