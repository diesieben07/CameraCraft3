package de.take_weiland.mods.cameracraft.client;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import de.take_weiland.mods.cameracraft.CCProxy;
import de.take_weiland.mods.cameracraft.client.gui.GuiPhotoName;
import de.take_weiland.mods.cameracraft.client.gui.GuiViewPhoto;
import de.take_weiland.mods.cameracraft.client.render.RenderInventoryPhoto;
import de.take_weiland.mods.cameracraft.client.render.RenderPoster;
import de.take_weiland.mods.cameracraft.client.render.RenderProcessor;
import de.take_weiland.mods.cameracraft.client.render.RenderScreen;
import de.take_weiland.mods.cameracraft.entity.EntityPaintable;
import de.take_weiland.mods.cameracraft.entity.EntityPoster;
import de.take_weiland.mods.cameracraft.entity.EntityScreen;
import de.take_weiland.mods.cameracraft.item.CCItem;
import de.take_weiland.mods.cameracraft.item.ItemDraw;
import de.take_weiland.mods.cameracraft.network.PacketTakenPhoto;
import de.take_weiland.mods.commons.util.Async;
import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.common.MinecraftForge;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class ClientProxy implements CCProxy {

    public static final ResourceLocation CONTROLS = new ResourceLocation("cameracraft", "textures/gui/controls.png");

    private PhotoTicker photoTicker;

    private Minecraft mc = Minecraft.getMinecraft();

    static int processorRenderId;

    @Override
    public void preInit() {
        photoTicker = new PhotoTicker();
        FMLCommonHandler.instance().bus().register(photoTicker);
        FMLCommonHandler.instance().bus().register(this);
        MinecraftForge.EVENT_BUS.register(this);

        RenderingRegistry.registerEntityRenderingHandler(EntityPoster.class, new RenderPoster());
        RenderingRegistry.registerEntityRenderingHandler(EntityScreen.class, new RenderScreen());

        MinecraftForgeClient.registerItemRenderer(CCItem.photo, new RenderInventoryPhoto());

        processorRenderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(processorRenderId, new RenderProcessor());
    }

    @Override
    public int getProcessorRenderId() {
        return processorRenderId;
    }

    @Override
    public CompletionStage<PacketTakenPhoto> handleStandardPhotoRequest() {
        CompletableFuture<PacketTakenPhoto> future = new CompletableFuture<>();
        photoTicker.schedulePhoto(future);
        return future;
    }

    @Override
    public void handleClientPhotoData(final long photoId, final InputStream in) {
        Async.commonExecutor().execute(() -> {
            try {
                PhotoDataCache.injectReceivedPhoto(photoId, in);
            } catch (IOException e) {
                CrashReport cr = new CrashReport("Receiving CameraCraft photodata", e);
                cr.makeCategory("Photo being received").addCrashSection("photoId", photoId);
                throw new ReportedException(cr);
            }
        });
    }

    @Override
    public void displayNamePhotoGui(String oldName) {
        mc().displayGuiScreen(new GuiPhotoName(oldName, input -> {
            // TODO Auto-generated method stub
        }));
    }

    @Override
    public void displayPhotoGui(long photoId, String displayName, boolean canRename) {
        mc().displayGuiScreen(new GuiViewPhoto(photoId, displayName, canRename));
    }

    @Override
    public void spawnAlkalineBubbleFX(double x, double y, double z, double motionX, double motionY, double motionZ) {
        mc().effectRenderer.addEffect(new EntityAlkalineBubbleFX(mc().theWorld, x, y, z, motionX, motionY, motionZ));
    }


    @SubscribeEvent
    public void connectionOpened(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        PhotoDataCache.invalidate();
    }

    @SubscribeEvent
    public void connectionClosed(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        PhotoDataCache.invalidate();
    }

    private static Minecraft mc() {
        return Minecraft.getMinecraft();
    }

    @SubscribeEvent
    public void mouseEvent(MouseEvent event) {
        if (event.button == 1) {
            if (mc.thePlayer.getCurrentEquippedItem() != null) {
                if (mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemDraw) {
                    if (mc.objectMouseOver != null) {
                        MovingObjectPosition mop = mc.objectMouseOver;
                        if (mop.entityHit != null) {
                            if (mop.entityHit instanceof EntityPaintable) {
                                EntityPaintable p = (EntityPaintable) mop.entityHit;
                                p.handlePaintPreCalc(mc.thePlayer, mop);
                            }
                        }
                    }
                }
            }

        }

    }
}
