package de.take_weiland.mods.cameracraft.client.texture;

import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;

import java.awt.image.BufferedImage;

/**
 * @author Intektor
 */
public class DynTexture extends DynamicTexture {

    protected int width, height;
    protected int[] textureData;
    protected BufferedImage image;

    public DynTexture(BufferedImage img) {
        this(img.getWidth(), img.getHeight());
        img.getRGB(0, 0, img.getWidth(), img.getHeight(), textureData, 0, img.getWidth());
        this.updateDynamicTexture();
        this.image = img;
    }

    public DynTexture(int width, int height) {
        super(width, height);
        this.width = width;
        this.height = height;
        this.textureData = new int[width * height];
        TextureUtil.allocateTexture(this.getGlTextureId(), width, height);
    }

    @Override
    public void updateDynamicTexture() {
        TextureUtil.uploadTexture(this.getGlTextureId(), textureData, width, height);
    }

    @Override
    public int[] getTextureData() {
        return textureData;
    }

    public void updateBufferedImage(BufferedImage img) {
        img.getRGB(0, 0, img.getWidth(), img.getHeight(), textureData, 0, img.getWidth());
        this.updateDynamicTexture();
        this.image = img;
    }

    public BufferedImage getImage() {
        return image;
    }
}
