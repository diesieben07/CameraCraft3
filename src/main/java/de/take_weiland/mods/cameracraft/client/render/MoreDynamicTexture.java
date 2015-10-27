package de.take_weiland.mods.cameracraft.client.render;

import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author Intektor
 */
public class MoreDynamicTexture extends DynamicTexture {

    private int[] dynamicTextureData;
    /** width of this icon in pixels */
    private int width;
    /** height of this icon in pixels */
    private int height;

    public MoreDynamicTexture(int p_i1271_1_, int p_i1271_2_) {
        super(p_i1271_1_, p_i1271_2_);
        this.dynamicTextureData = new int[p_i1271_1_ * p_i1271_2_];
        TextureUtil.allocateTexture(this.getGlTextureId(), p_i1271_1_, p_i1271_2_);
    }

    public MoreDynamicTexture(BufferedImage p_i1270_1_) {
        super(p_i1270_1_);
        p_i1270_1_.getRGB(0, 0, p_i1270_1_.getWidth(), p_i1270_1_.getHeight(), this.dynamicTextureData, 0, p_i1270_1_.getWidth());
        this.width = p_i1270_1_.getWidth();
        this.height = p_i1270_1_.getHeight();
    }

    public void loadTexture(IResourceManager p_110551_1_) throws IOException {}

    public void updateDynamicTexture()
    {
        TextureUtil.uploadTexture(this.getGlTextureId(), this.dynamicTextureData, this.width, this.height);
    }

    public int[] getTextureData()
    {
        return this.dynamicTextureData;
    }

    public void updateBufferedImage(BufferedImage buf) {
        width = buf.getWidth();
        height = buf.getHeight();
        buf.getRGB(0, 0, buf.getWidth(), buf.getHeight(), this.dynamicTextureData, 0, buf.getWidth());
        this.updateDynamicTexture();
    }
}
