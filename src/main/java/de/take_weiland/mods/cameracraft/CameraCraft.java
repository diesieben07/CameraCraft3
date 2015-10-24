package de.take_weiland.mods.cameracraft;

import com.google.common.base.Throwables;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInterModComms.IMCEvent;
import cpw.mods.fml.common.event.FMLInterModComms.IMCMessage;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import de.take_weiland.mods.cameracraft.api.CameraCraftApi;
import de.take_weiland.mods.cameracraft.api.CameraCraftApiHandler;
import de.take_weiland.mods.cameracraft.blocks.CCBlock;
import de.take_weiland.mods.cameracraft.entity.EntityPoster;
import de.take_weiland.mods.cameracraft.gui.CCGuis;
import de.take_weiland.mods.cameracraft.item.CCItem;
import de.take_weiland.mods.cameracraft.item.CameraType;
import de.take_weiland.mods.cameracraft.network.*;
import de.take_weiland.mods.cameracraft.worldgen.CCWorldGen;
import de.take_weiland.mods.commons.net.Network;
import de.take_weiland.mods.commons.net.PacketConstructor;
import de.take_weiland.mods.commons.net.PacketHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

@Mod(modid = CameraCraft.MOD_ID, name = CameraCraft.MOD_NAME, version = CameraCraft.VERSION)
public final class CameraCraft {

    public static final String MOD_ID = CameraCraftApiHandler.CAMERACRAFT_MODID;
    static final String MOD_NAME = "CameraCraft";
    static final String VERSION = "@VERSION@";
    private static final String CLIENT_ENV = "de.take_weiland.mods.cameracraft.client.ClientProxy";
    private static final String SERVER_ENV = "de.take_weiland.mods.cameracraft.server.ServerProxy";

    @Instance(MOD_ID)
    public static CameraCraft instance;

    @SidedProxy(clientSide = CLIENT_ENV, serverSide = SERVER_ENV)
    public static CCProxy proxy;

    public static Configuration config;

    public static org.apache.logging.log4j.Logger logger;

    public static CameraCraftApi api;

    public static Executor executor;

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

        api = new ApiImpl();

        logger = event.getModLog();

        setupThreads();

        Network.newSimpleChannel("CameraCraft")
                .register(0, PacketClientAction::new, PacketClientAction::handle)
                .register(1, PacketPhotoName::new, PacketPhotoName::handle)
                .registerWithAsyncResponse(2, (PacketConstructor<PacketRequestStandardPhoto>) PacketRequestStandardPhoto::new, (PacketConstructor<PacketTakenPhoto>) PacketTakenPhoto::new, (PacketHandler.WithAsyncResponse<PacketRequestStandardPhoto, PacketTakenPhoto>) PacketRequestStandardPhoto::handle)
                .register(3, (PacketConstructor<PacketClientRequestPhoto>) PacketClientRequestPhoto::new, (PacketConstructor<PacketPhotoData>) PacketPhotoData::read, (PacketHandler.WithResponse<PacketClientRequestPhoto, PacketPhotoData>) PacketClientRequestPhoto::handle)
                .register(4, (PacketConstructor<PacketPrintJobs>) PacketPrintJobs::new, (PacketHandler<PacketPrintJobs>) PacketPrintJobs::handle);

        CCBlock.createBlocks();
        CCItem.init();

        EntityRegistry.registerModEntity(EntityPoster.class, "poster", 0, this, 160, Integer.MAX_VALUE, false);

        if (enableOreGeneration) {
            GameRegistry.registerWorldGenerator(new CCWorldGen(), 0);
        }

        NetworkRegistry.INSTANCE.registerGuiHandler(this, new CCGuis.Handler());

        CCRegistry.addRecipes();
        CCRegistry.doMiscRegistering();

        MinecraftForge.EVENT_BUS.register(new CCEventHandler());

        if (config.hasChanged()) {
            config.save();
        }

        proxy.preInit();
    }

    private static void setupThreads() {
        // TODO
        executor = ForkJoinPool.commonPool();
    }

    public static void printErrorMessage(EntityPlayer player, String msg, Throwable x) {
        logger.error(msg, x);
        ChatComponentText chatComponent = new ChatComponentText(msg);
        chatComponent.getChatStyle().setColor(EnumChatFormatting.RED);
        player.addChatComponentMessage(chatComponent);
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
                        logger.warn(String.format("Invalid RequestApi message from %s. Must be a String message", message.getSender()));
                    }
                } catch (ClassCastException e) {
                    logger.warn(String.format("Class %s must implement CameraCraftApiHandler.", message.getStringValue()));
                } catch (ReflectiveOperationException e) {
                    logger.warn(String.format("ReflectiveOperationException during RequestApi processing from %s", message.getSender()));
                    e.printStackTrace();
                } catch (Throwable t) {
                    logger.error(String.format("Unexpected Exception during RequestApi processing from %s", message.getSender()));
                    Throwables.propagate(t);
                }
            }
        }
    }

}