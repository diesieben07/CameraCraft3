package de.take_weiland.mods.cameracraft.client;

import java.awt.*;

/**
 * @author Intektor
 */
public class CColor {

    protected Color color;

    private int looper, red, green, blue;


    public CColor(Color color) {
        this.color = color;
        red = color.getRed();
        green = color.getGreen();
        blue = color.getBlue();
    }

    public void loopThroughColor(int jump) {
        if (looper < 1 << 12) {
            red = ((looper >> 8) & 0xf) * 0x11;
            green = ((looper >> 4) & 0xf) * 0x11;
            blue = (looper & 0xf) * 0x11;
            color = new Color(((255 & 0xFF) << 24) | ((red & 0xFF) << 16) | ((green & 0xFF) << 8) | ((blue & 0xFF) << 0));
        }else{
            looper = 0;
        }
        looper += jump;
    }

    public Color getColor() {
        return color;
    }
}
