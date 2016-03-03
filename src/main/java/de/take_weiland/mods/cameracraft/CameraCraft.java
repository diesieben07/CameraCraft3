package de.take_weiland.mods.cameracraft;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import de.take_weiland.mods.cameracraft.api.CameraCraftApi;
import de.take_weiland.mods.cameracraft.blocks.CCBlock;
import de.take_weiland.mods.cameracraft.db.DatabaseImpl;
import de.take_weiland.mods.cameracraft.entity.EntityPoster;
import de.take_weiland.mods.cameracraft.entity.EntityScreen;
import de.take_weiland.mods.cameracraft.entity.EntityVideoCamera;
import de.take_weiland.mods.cameracraft.gui.CCGuis;
import de.take_weiland.mods.cameracraft.item.CCArmor;
import de.take_weiland.mods.cameracraft.item.CCItem;
import de.take_weiland.mods.cameracraft.item.CameraType;
import de.take_weiland.mods.cameracraft.network.*;
import de.take_weiland.mods.cameracraft.worldgen.CCWorldGen;
import de.take_weiland.mods.commons.net.Network;
import de.take_weiland.mods.commons.util.Players;
import de.take_weiland.mods.commons.util.Scheduler;
import de.take_weiland.mods.commons.util.Sides;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

@Mod(modid = CameraCraft.MOD_ID, name = CameraCraft.MOD_NAME, version = CameraCraft.VERSION)
public final class CameraCraft {

    public static final  String MOD_ID     = "cameracraft";
    public static final  String modid      = "cameracraft";
    public static final  String MODID      = modid;
    static final         String MOD_NAME   = "CameraCraft";
    static final         String VERSION    = "@VERSION@";
    private static final String CLIENT_ENV = "de.take_weiland.mods.cameracraft.client.ClientProxy";
    private static final String SERVER_ENV = "de.take_weiland.mods.cameracraft.server.ServerProxy";

    @Instance(MOD_ID)
    public static CameraCraft instance;

    @SidedProxy(clientSide = CLIENT_ENV, serverSide = SERVER_ENV)
    public static CCProxy proxy;

    public static Configuration config;

    public static org.apache.logging.log4j.Logger logger;

    public static final ApiImpl apiInternal = new ApiImpl();
    public static final CameraCraftApi api = apiInternal;

    public static CreativeTabs tab = new CreativeTabs("cameracraft") {

        @Override
        public Item getTabIconItem() {
            return CCItem.camera;
        }

        @Override
        public ItemStack getIconItemStack() {
            return CCItem.camera.getStack(CameraType.FILM);
        }

    };

    public static boolean enableOreGeneration;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();

        enableOreGeneration = config.getBoolean("enableOreGen", Configuration.CATEGORY_GENERAL, true, "Whether to enable ore generation");

        logger = event.getModLog();

        Network.newSimpleChannel("CameraCraft")
                .register(0, PacketClientAction::new, PacketClientAction::handle)
                .register(1, PacketPhotoName::new, PacketPhotoName::handle)
                .register(2, PacketRequestStandardPhoto::new, PacketImageResponse::new, PacketRequestStandardPhoto::handle)
                .register(3, PacketClientRequestPhoto::new, PacketPhotoData::new, PacketClientRequestPhoto::handle)
                .register(4, PacketPrintJobs::new, PacketPrintJobs::handle)
                .register(5, PacketPaint::new, PacketPaint::handle)
                .register(6, PacketGuiPenButton::new, PacketGuiPenButton::handle)
                .register(7, PacketRequestPrintJob::new, PacketRequestPrintJob::handle)
                .register(8, PacketStreamID::new, PacketStreamID::handle)
                .register(9, PacketDrawingBoard::new, PacketDrawingBoard::handle)
                .register(10, PacketMemoryHandlerDeletePicture::new, PacketMemoryHandlerDeletePicture::handle)
                .register(11, PacketMemoryHandlerRename::new, PacketMemoryHandlerRename::handle)
                .register(12, PacketNewViewport::new, PacketNewViewport::handle)
                .register(13, PacketKillViewport::new, PacketKillViewport::handle)
                .register(14, PacketRequestViewportPhoto::new, PacketImageResponse::new, PacketRequestViewportPhoto::handle);


        CCBlock.createBlocks();
        CCItem.init();
        CCArmor.init();

        EntityRegistry.registerModEntity(EntityPoster.class, "poster", 0, this, 160, Integer.MAX_VALUE, false);
        EntityRegistry.registerModEntity(EntityVideoCamera.class, "video.camera", 1, this, 160, Integer.MAX_VALUE, false);
        EntityRegistry.registerModEntity(EntityScreen.class, "screen", 2, this, 160, Integer.MAX_VALUE, false);

        if (enableOreGeneration) {
            GameRegistry.registerWorldGenerator(new CCWorldGen(), 0);
        }

        NetworkRegistry.INSTANCE.registerGuiHandler(this, new CCGuis.Handler());

        CCRegistry.addRecipes();
        CCRegistry.doMiscRegistering();

        ForgeEventHandler eventHandler = new ForgeEventHandler();
        Scheduler.forSide(Sides.environment()).execute(eventHandler);
        MinecraftForge.EVENT_BUS.register(eventHandler);
        FMLCommonHandler.instance().bus().register(new FMLEventHandler());

        proxy.preInit();

        api.registerViewportProviderFactory(PlayerViewportFactory.IDENTIFIER, new PlayerViewportFactory());
        api.registerViewportProviderFactory("test123", viewport -> null);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        apiInternal.bake();

        if (config.hasChanged()) {
            config.save();
        }
    }

    public static void printErrorMessage(EntityPlayer player, String msg, Throwable x) {
        logger.error(msg, x);
        ChatComponentText chatComponent = new ChatComponentText(msg);
        chatComponent.getChatStyle().setColor(EnumChatFormatting.RED);
        player.addChatComponentMessage(chatComponent);
    }

    static boolean serverStartingUp = false;

    @EventHandler
    public void serverAboutToStart(FMLServerAboutToStartEvent event) {
        serverStartingUp = true;
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        serverStartingUp = false;
    }

    @EventHandler
    public void serverStopping(FMLServerStoppingEvent event) {
        ForgeEventHandler.setDatabase(null);
        if (!MinecraftServer.getServer().isDedicatedServer()) {
            // fix logout event not firing in SP, not needed in 1.8
            Players.getAll().forEach(FMLEventHandler::handleLogout);
        }
    }

    public static DatabaseImpl currentDatabase() {
        return ForgeEventHandler.currentDb;
    }

}