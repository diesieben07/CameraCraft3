package de.take_weiland.mods.cameracraft;

import java.util.concurrent.Executors;
import java.util.logging.Logger;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;

import com.google.common.base.Throwables;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms.IMCEvent;
import cpw.mods.fml.common.event.FMLInterModComms.IMCMessage;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import de.take_weiland.mods.cameracraft.api.CameraCraftApi;
import de.take_weiland.mods.cameracraft.api.CameraCraftApiHandler;
import de.take_weiland.mods.cameracraft.blocks.CCBlock;
import de.take_weiland.mods.cameracraft.entity.EntityPoster;
import de.take_weiland.mods.cameracraft.gui.CCGuis;
import de.take_weiland.mods.cameracraft.item.CCItem;
import de.take_weiland.mods.cameracraft.item.CameraType;
import de.take_weiland.mods.cameracraft.network.CCPackets;
import de.take_weiland.mods.cameracraft.worldgen.CCWorldGen;
import de.take_weiland.mods.commons.config.ConfigInjector;
import de.take_weiland.mods.commons.config.GetProperty;
import de.take_weiland.mods.commons.network.PacketTransport;
import de.take_weiland.mods.commons.network.PacketTransports;
import de.take_weiland.mods.commons.util.ItemStacks;

@Mod(modid = CameraCraft.MOD_ID, name = CameraCraft.MOD_NAME, version = CameraCraft.VERSION)
@NetworkMod(serverSideRequired = true, clientSideRequired = true)
public final class CameraCraft {

	static final String MOD_ID = CameraCraftApiHandler.CAMERACRAFT_MODID;
	static final String MOD_NAME = "CameraCraft";
	static final String VERSION = "@VERSION@";
	private static final String CLIENT_ENV = "de.take_weiland.mods.cameracraft.client.EnvironmentClient";
	private static final String SERVER_ENV = "de.take_weiland.mods.cameracraft.server.EnvironmentServer";
	
	@Instance(MOD_ID)
	public static CameraCraft instance;
	
	@SidedProxy(clientSide = CLIENT_ENV, serverSide = SERVER_ENV)
	public static Environment env;
	
	public static Configuration config;
	
	public static Logger logger;
	
	public static CameraCraftApi api;
	
	public static PacketTransport packetTransport;
	
	public static ListeningExecutorService executor;
	
	public static CreativeTabs tab = new CreativeTabs("cameracraft") {

		@Override
		public ItemStack getIconItemStack() {
			return ItemStacks.of(CameraType.FILM);
		}
		
	};
	
	@GetProperty
	public static boolean enableOreGeneration;
	
	@GetProperty(comment = "How many Threads CameraCraft should use for Performance optimizations")
	public static int maxThreadCount = 3;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		
		api = new ApiImpl();
		
		logger = event.getModLog();
		
		ConfigInjector.inject(config, CameraCraft.class, false, false);
		
		executor = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(maxThreadCount));
		
		packetTransport = PacketTransports.withPacket131(this, CCPackets.class);
		
		CCBlock.createBlocks();
		CCItem.createItems();
		
		EntityRegistry.registerModEntity(EntityPoster.class, "poster", 0, this, 160, Integer.MAX_VALUE, false);
		
		if (enableOreGeneration) {
			MinecraftForge.ORE_GEN_BUS.register(new CCWorldGen());
		}
		
		NetworkRegistry.instance().registerGuiHandler(this, new CCGuis.Handler());
		
		CCRegistry.addRecipes();
		CCRegistry.doMiscRegistering();
		
		MinecraftForge.EVENT_BUS.register(new CCEventHandler());
		TickRegistry.registerTickHandler(new CCPlayerTickHandler(), Side.SERVER);
		
		if (config.hasChanged()) {
			config.save();
		}
		
		env.preInit();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		
	}
	
	@EventHandler
	public void handleModComms(IMCEvent event) {
		for (IMCMessage message : event.getMessages()) {
			if (message.key.equals(CameraCraftApiHandler.IMC_REQUEST_KEY)) {
				try {
					if (message.isStringMessage()) {
						Class<?> handlerClass = Class.forName(message.getStringValue());
						CameraCraftApiHandler handler = (CameraCraftApiHandler) handlerClass.newInstance();
						handler.injectApi(api);
					} else {
						logger.warning(String.format("Invalid RequestApi message from %s. Must be a String message", message.getSender()));
					}
				} catch (ClassCastException e) {
					logger.warning(String.format("Class %s must implement CameraCraftApiHandler.", message.getStringValue()));
				} catch (ReflectiveOperationException e) {
					logger.warning(String.format("ReflectiveOperationException during RequestApi processing from %s", message.getSender()));
					e.printStackTrace();
				} catch (Throwable t) {
					logger.severe(String.format("Unexpected Exception during RequestApi processing from %s", message.getSender()));
					Throwables.propagate(t);
				}
			}
		}
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		
	}

}
