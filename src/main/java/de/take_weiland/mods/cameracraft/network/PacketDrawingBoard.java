package de.take_weiland.mods.cameracraft.network;

import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.cameracraft.api.photo.PhotoItem;
import de.take_weiland.mods.cameracraft.db.DatabaseImpl;
import de.take_weiland.mods.cameracraft.gui.ContainerDrawingBoard;
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
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;

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
                DatabaseImpl db = CameraCraft.currentDatabase();

                db.getImageAsync(photoId)
                        .thenComposeAsync(img -> {
                            BufferedImage newImg = cloneImage(img);
                            Graphics g = newImg.getGraphics();
                            g.drawImage(overlay, 0, 0, null);
                            g.dispose();
                            return db.saveNewImage(newImg);
                        }, de.take_weiland.mods.commons.util.Async.commonExecutor())

                        .thenAcceptAsync(newId -> {
                            ((PhotoItem) item).setPhotoId(stack, newId);
                            ((ContainerDrawingBoard) container).inventory().setInventorySlotContents(0, stack);
                            player.closeScreen();
                        }, Scheduler.server());
            }
        }
    }

    // http://stackoverflow.com/a/3514297
    static BufferedImage cloneImage(BufferedImage img) {
        ColorModel cm = img.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = img.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

}
