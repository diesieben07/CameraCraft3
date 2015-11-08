package de.take_weiland.mods.cameracraft.client;

import de.take_weiland.mods.commons.asm.MCPNames;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.awt.image.BufferedImage;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.Random;

public final class ClientUtil {

    private ClientUtil() {
    }

    private static ByteBuffer ssBuffer;

    public static byte[] rawScreenshot(Minecraft mc) {
        GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

        int width = mc.displayWidth;
        int height = mc.displayHeight;
        int byteCount = width * height * 3;

        if (ssBuffer == null || ssBuffer.capacity() < byteCount) {
            ssBuffer = BufferUtils.createByteBuffer(byteCount);
        }

        ssBuffer.clear();
        // read the BGR values into the overlay
        GL11.glReadPixels(0, 0, width, height, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, ssBuffer);

        ssBuffer.rewind();

        byte[] bytes = new byte[byteCount];
        ssBuffer.get(bytes);

        return bytes;
    }

    private static final MethodHandle dynTexDataGet;
    private static final MethodHandle dynTexWidthGet;
    private static final MethodHandle dynTexHeightGet;

    static {
        try {
            Field field = DynamicTexture.class.getDeclaredField(MCPNames.field("field_110566_b"));
            field.setAccessible(true);
            dynTexDataGet = MethodHandles.publicLookup().unreflectGetter(field);
            Field field2 = DynamicTexture.class.getDeclaredField("width");
            field2.setAccessible(true);
            dynTexWidthGet = MethodHandles.publicLookup().unreflectGetter(field2);
            Field field3 = DynamicTexture.class.getDeclaredField("height");
            field3.setAccessible(true);
            dynTexHeightGet = MethodHandles.publicLookup().unreflectGetter(field3);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateDynamicTexture(DynamicTexture texture, BufferedImage image) {
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), getDynamicTextureRGB(texture), 0, image.getWidth());
        texture.updateDynamicTexture();
    }

    public static int[] getDynamicTextureRGB(DynamicTexture texture) {
        int[] textureData;
        try {
            textureData = (int[]) dynTexDataGet.invokeExact(texture);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
        return textureData;
    }

    public static int getDynamicTextureWidth(DynamicTexture texture) {
        int width;
        try {
            width = (int) dynTexWidthGet.invokeExact(texture);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
        return width;
    }

    public static int getDynamicTextureHeight(DynamicTexture texture) {
        int height;
        try {
            height = (int) dynTexWidthGet.invokeExact(texture);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
        return height;
    }

    public static BufferedImage getBufferedImagefromDynamicTexture(DynamicTexture texture) {
        BufferedImage image = new BufferedImage(getDynamicTextureWidth(texture), getDynamicTextureHeight(texture), BufferedImage.TYPE_4BYTE_ABGR);
        image.setRGB(0, 0, image.getHeight(), image.getWidth(), getDynamicTextureRGB(texture), 0, image.getWidth());
        return image;
    }

    private static Random r = new Random();

    public static EnumChatFormatting getRandomFontColor() {
        EnumChatFormatting e = null;
        int i = r.nextInt(16);
        switch(i){
            case 0:
                e = EnumChatFormatting.AQUA;
                break;
            case 1:
                e = EnumChatFormatting.BLACK;
                break;
            case 2:
                e = EnumChatFormatting.BLUE;
                break;
            case 3:
                e = EnumChatFormatting.DARK_AQUA;
                break;
            case 4:
                e = EnumChatFormatting.DARK_BLUE;
                break;
            case 5:
                e = EnumChatFormatting.DARK_GRAY;
                break;
            case 6:
                e = EnumChatFormatting.DARK_GREEN;
                break;
            case 7:
                e = EnumChatFormatting.DARK_PURPLE;
                break;

            case 8:
                e = EnumChatFormatting.DARK_RED;
                break;
            case 9:
                e = EnumChatFormatting.GOLD;
                break;
            case 10:
                e = EnumChatFormatting.GRAY;
                break;
            case 11:
                e = EnumChatFormatting.GREEN;
                break;
            case 12:
                e = EnumChatFormatting.LIGHT_PURPLE;
                break;
            case 13:
                e = EnumChatFormatting.RED;
                break;
            case 14:
                e = EnumChatFormatting.WHITE;
                break;
            case 15:
                e = EnumChatFormatting.YELLOW;
                break;
        }
        return e;
    }

    public static String colorEveryCharacterInString(String string) {
        String randString = "";
        char[] c = string.toCharArray();
        for(char sub : c) {
            randString += getRandomFontColor() + (sub + "");
        }
        return randString;
    }
}
