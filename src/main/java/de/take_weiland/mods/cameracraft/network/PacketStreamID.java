package de.take_weiland.mods.cameracraft.network;

import de.take_weiland.mods.cameracraft.CCPlayerData;
import de.take_weiland.mods.cameracraft.entity.EntityScreen;
import de.take_weiland.mods.cameracraft.entity.EntityVideoCamera;
import de.take_weiland.mods.commons.net.MCDataInput;
import de.take_weiland.mods.commons.net.MCDataOutput;
import de.take_weiland.mods.commons.net.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author Intektor
 */
public class PacketStreamID implements Packet {

    private String streamID;

    public PacketStreamID(String streamID) {
        this.streamID = streamID;
    }

    public PacketStreamID(MCDataInput in) {
        streamID = in.readString();
    }

    @Override
    public void writeTo(MCDataOutput out) {
        out.writeString(streamID);
    }

    public void handle(EntityPlayer player) {
        Entity entity = player.worldObj.getEntityByID(CCPlayerData.get(player).getLastClickedEntityID());
        if(entity != null) {
            if(entity instanceof EntityVideoCamera) {
                EntityVideoCamera camera = (EntityVideoCamera) entity;
                camera.setStreamID(streamID);
            }else if(entity instanceof EntityScreen) {
                EntityScreen screen = (EntityScreen) entity;
                screen.setStreamID(streamID);
            }
        }
    }
}
