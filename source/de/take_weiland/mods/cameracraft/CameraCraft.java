package de.take_weiland.mods.cameracraft;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import de.take_weiland.mods.cameracraft.blocks.CCBlock;
import de.take_weiland.mods.cameracraft.item.CCItem;
import de.take_weiland.mods.cameracraft.item.CameraType;
import de.take_weiland.mods.cameracraft.networks.CCPackets;
import de.take_weiland.mods.cameracraft.tileentity.TilePhotoProcessor;
import de.take_weiland.mods.commons.network.ModPacketHandler;
import de.take_weiland.mods.commons.util.Items;

@Mod(modid = CameraCraft.MOD_ID, name = CameraCraft.MOD_NAME, version = CameraCraft.VERSION)
@NetworkMod(serverSideRequired = true, clientSideRequired = true)
public final class CameraCraft {

	static final String MOD_ID = "CameraCraft";
	static final String MOD_NAME = "CameraCraft";
	static final String VERSION = "3.0b";
	private static final String CLIENT_HANDLER = "de.take_weiland.mods.cameracraft.client.ClientHandler";
	private static final String SERVER_HANDLER = "de.take_weiland.mods.cameracraft.server.ServerHandler";
	
	@Instance
	public static CameraCraft instance;
	
	@SidedProxy(clientSide = CLIENT_HANDLER, serverSide = SERVER_HANDLER)
	public static CCSidedHandler proxy;
	
	public static Configuration config;
	
	public static CreativeTabs tab = new CreativeTabs("cameracraft") {

		@Override
		public ItemStack getIconItemStack() {
			return Items.getStack(CCItem.camera, CameraType.FILM);
		}
		
	};
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		
		ModPacketHandler.setupNetworking(this, CCPackets.values());
		CCItem.createItems();
		CCBlock.createBlocks();
		
		NetworkRegistry.instance().registerGuiHandler(this, new CCGuis.Handler());
		
		GameRegistry.registerTileEntity(TilePhotoProcessor.class, "cameracraft.processor");
		
		if (config.hasChanged()) {
			config.save();
		}
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		
	}

}
