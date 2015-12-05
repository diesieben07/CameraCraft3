package de.take_weiland.mods.cameracraft.network;

import de.take_weiland.mods.cameracraft.photo.DatabaseImpl;
import de.take_weiland.mods.commons.net.MCDataInput;
import de.take_weiland.mods.commons.net.MCDataOutput;
import de.take_weiland.mods.commons.net.Packet;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.ReportedException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

public class PacketPhotoData implements Packet.Response {

    private final long photoId;
    private final BufferedImage image;

    public PacketPhotoData(long photoId) {
        this(photoId, null);
    }

    public PacketPhotoData(long photoId, BufferedImage image) {
        this.photoId = photoId;
        this.image = image;
    }

    @Override
    public void writeTo(MCDataOutput out) {
        out.writeLong(photoId);
        try (InputStream in = DatabaseImpl.current.openImageStream(photoId)) {
            out.copyFrom(in);
        } catch (IOException e) {
            CrashReport cr = new CrashReport("Reading CameraCraft Photo File", e);
            cr.makeCategory("Photo being read").addCrashSection("PhotoId", photoId);
            throw new ReportedException(cr);
        }
    }

    public static PacketPhotoData read(MCDataInput in) {
        long photoId = in.readLong();
        BufferedImage image;
        try {
            image = ImageIO.read(in.asInputStream());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return new PacketPhotoData(photoId, image);
    }

    public BufferedImage getImage() {
        BufferedImage result;
        if (image == null) {
            result = DatabaseImpl.current.loadImage(photoId);
        } else {
            result = image;
        }
        return result;
    }

}