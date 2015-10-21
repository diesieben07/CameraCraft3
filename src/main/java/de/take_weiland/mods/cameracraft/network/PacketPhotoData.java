package de.take_weiland.mods.cameracraft.network;

import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.commons.net.MCDataInput;
import de.take_weiland.mods.commons.net.MCDataOutput;
import de.take_weiland.mods.commons.net.Packet;
import net.minecraft.crash.CrashReport;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ReportedException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PacketPhotoData implements Packet {

    private final long photoId;
    private final File file;

    public PacketPhotoData(long photoId, File file) {
        this.photoId = photoId;
        this.file = file;
    }

    @Override
    public void writeTo(MCDataOutput out) {
        out.writeLong(photoId);
        try (FileInputStream in = new FileInputStream(file)) {
            out.copyFrom(in);
        } catch (IOException e) {
            CrashReport cr = new CrashReport("Reading CameraCraft Photo File", e);
            cr.makeCategory("Photo being read").addCrashSection("PhotoId", photoId);
            throw new ReportedException(cr);
        }
    }

    public static PacketPhotoData read(MCDataInput in) {
        int photoId = in.readInt();
        CameraCraft.proxy.handleClientPhotoData(photoId, in.asInputStream());
        return null;
    }

    public static void handle(PacketPhotoData packet, EntityPlayer player) {
        if (packet != null) {
            try {
                CameraCraft.proxy.handleClientPhotoData(packet.photoId, new FileInputStream(packet.file));
            } catch (FileNotFoundException e) {
                throw new IllegalStateException("Photo file was deleted unexpectedly!", e);
            }
        }
    }

}