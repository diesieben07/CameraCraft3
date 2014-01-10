package de.take_weiland.mods.cameracraft.client;

import java.io.IOException;
import java.io.InputStream;

import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.NetLoginHandler;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet1Login;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ReportedException;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import de.take_weiland.mods.cameracraft.CCSounds;
import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.cameracraft.Environment;
import de.take_weiland.mods.cameracraft.blocks.CCBlock;
import de.take_weiland.mods.cameracraft.client.gui.GuiPhotoName;
import de.take_weiland.mods.cameracraft.client.gui.GuiViewPhoto;
import de.take_weiland.mods.cameracraft.client.render.RenderBlockCable;
import de.take_weiland.mods.commons.util.Consumer;
import de.take_weiland.mods.commons.util.Scheduler;

public class EnvironmentClient implements Environment, IConnectionHandler {

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
		NetworkRegistry.instance().registerConnectionHandler(this);
	}

	@Override
	public void onPhotoRequest(int transferId) {
		rth.schedulePhoto(transferId);
	}

	@Override
	public void handleClientPhotoData(final String photoId, final InputStream in) {
		CameraCraft.executor.execute(new Runnable() {
			
			@Override
			public void run() {
				try {
					PhotoDataCache.injectReceivedPhoto(photoId, in);
				} catch (IOException e) {
					CrashReport cr = new CrashReport("Receiving CameraCraft photodata", e);
					cr.makeCategory("Photo being received").addCrashSection("photoId", photoId);
					Scheduler.client().throwInMainThread(new ReportedException(cr));
				}
			}
		});
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
	
	@Override
	public void displayPhotoGui(String photoId) {
		mc.displayGuiScreen(new GuiViewPhoto(photoId));
	}

	@Override
	public void spawnAlkalineBubbleFX(double x, double y, double z, double motionX, double motionY, double motionZ) {
		mc.effectRenderer.addEffect(new EntityAlkalineBubbleFX(mc.theWorld, x, y, z, motionX, motionY, motionZ));
	}
	
	@ForgeSubscribe
	public void onSoundLoad(SoundLoadEvent event) {
		for (CCSounds sound : CCSounds.values()) {
			sound.register(event.manager);
		}
	}

	@Override
	public void connectionOpened(NetHandler netClientHandler, String server, int port, INetworkManager manager) {
		PhotoDataCache.invalidate();
	}

	@Override
	public void connectionOpened(NetHandler netClientHandler, MinecraftServer server, INetworkManager manager) {
		PhotoDataCache.invalidate();
	}
	
	@Override
	public void connectionClosed(INetworkManager manager) {
		if (manager == mc.getNetHandler().getNetManager()) {
			PhotoDataCache.invalidate();
		}
	}
	
	@Override
	public void playerLoggedIn(Player player, NetHandler netHandler, INetworkManager manager) { }

	@Override
	public String connectionReceived(NetLoginHandler netHandler, INetworkManager manager) {
		return null;
	}

	@Override
	public void clientLoggedIn(NetHandler clientHandler, INetworkManager manager, Packet1Login login) { }

}
