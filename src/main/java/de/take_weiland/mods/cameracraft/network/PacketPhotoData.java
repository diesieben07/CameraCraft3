package de.take_weiland.mods.cameracraft.network;

import de.take_weiland.mods.commons.net.MCDataInput;
import de.take_weiland.mods.commons.net.MCDataOutput;
import de.take_weiland.mods.commons.net.Packet;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class PacketPhotoData implements Packet.Response {

    private final BufferedImage image;

    public PacketPhotoData(BufferedImage image) {
        this.image = image;
    }

    public PacketPhotoData(MCDataInput in) throws IOException {
        this.image = ImageIO.read(in.asInputStream());
    }

    @Override
    public void writeTo(MCDataOutput out) throws Exception {
        ImageIO.write(image, "PNG", out.asOutputStream());
    }

    public BufferedImage getImage() {
        return image;
    }

}