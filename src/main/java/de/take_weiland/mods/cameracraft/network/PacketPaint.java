package de.take_weiland.mods.cameracraft.network;

import de.take_weiland.mods.cameracraft.entity.EntityPaintable;
import de.take_weiland.mods.commons.net.MCDataInput;
import de.take_weiland.mods.commons.net.MCDataOutput;
import de.take_weiland.mods.commons.net.Packet;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author Intektor
 */
public class PacketPaint implements Packet {

    public int id, colorCode;
    public double x, y;

    public PacketPaint(int entityID, double posX, double posY, int colorCode) {
        id = entityID;
        x = posX;
        y = posY;
        this.colorCode = colorCode;
    }

    public PacketPaint(MCDataInput in) {
        id = in.readInt();
        x = in.readDouble();
        y = in.readDouble();
        colorCode = in.readInt();
    }

    @Override
    public void writeTo(MCDataOutput out) {
        out.writeInt(id);
        out.writeDouble(x);
        out.writeDouble(y);
        out.writeInt(colorCode);
    }

    public void handle(EntityPlayer player) {
        EntityPaintable entity = (EntityPaintable) player.worldObj.getEntityByID(id);
        if (entity != null) {
            entity.paint(player, x, y, colorCode, player.getCurrentEquippedItem());
        }
    }
}
