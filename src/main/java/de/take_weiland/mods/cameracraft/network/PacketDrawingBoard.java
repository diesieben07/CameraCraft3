package de.take_weiland.mods.cameracraft.network;

import de.take_weiland.mods.cameracraft.api.photo.PhotoItem;
import de.take_weiland.mods.cameracraft.gui.ContainerDrawingBoard;
import de.take_weiland.mods.cameracraft.db.DatabaseImpl;
import de.take_weiland.mods.commons.net.MCDataInput;
import de.take_weiland.mods.commons.net.MCDataOutput;
import de.take_weiland.mods.commons.net.Packet;
import de.take_weiland.mods.commons.net.ProtocolException;
import de.take_weiland.mods.commons.util.Scheduler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.ForkJoinPool;

/**
 * @author diesieben07
 */
public class PacketDrawingBoard implements Packet {

    private final int           windowId;
    private final BufferedImage overlay;

    public PacketDrawingBoard(int windowId, BufferedImage overlay) {
        this.windowId = windowId;
        this.overlay = overlay;
    }

    public PacketDrawingBoard(MCDataInput in) {
        try {
            overlay = ImageIO.read(in.asInputStream());
        } catch (IOException e) {
            throw new ProtocolException(e);
        }
        windowId = in.readByte();
    }

    @Override
    public void writeTo(MCDataOutput out) {
        try {
            ImageIO.write(overlay, "png", out.asOutputStream());
        } catch (IOException e) {
            throw new ProtocolException(e);
        }
        out.writeByte(windowId);
    }

    public void handle(EntityPlayer player) {
        Container container = player.openContainer;
        if (container.windowId == windowId && container instanceof ContainerDrawingBoard) {
            ItemStack stack = ((ContainerDrawingBoard) container).inventory().getStackInSlot(0);
            Item item;
            if (stack != null && (item = stack.getItem()) instanceof PhotoItem) {
                long photoId = ((PhotoItem) item).getPhotoId(stack);
                ForkJoinPool.commonPool().execute(() -> {
                    BufferedImage image = DatabaseImpl.current.loadImage(photoId);
                    Graphics g = image.getGraphics();
                    g.drawImage(overlay, 0, 0, null);
                    g.dispose();

                    long newId = DatabaseImpl.current.saveNewImage(image);

                    Scheduler.server().execute(() -> {
                        ((PhotoItem) item).setPhotoId(stack, newId);
                        ((ContainerDrawingBoard) container).inventory().setInventorySlotContents(0, stack);
                        player.closeScreen();
                    });
                });
            }
        }
    }
}
