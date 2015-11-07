package de.take_weiland.mods.cameracraft.client;

import de.take_weiland.mods.commons.asm.MCPNames;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.awt.image.BufferedImage;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;

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
		// read the BGR values into the image
		GL11.glReadPixels(0, 0, width, height, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, ssBuffer);
		
		ssBuffer.rewind();
		
		byte[] bytes = new byte[byteCount];
		ssBuffer.get(bytes);
		
		return bytes;
	}

    private static final MethodHandle dynTexDataGet;

    static {
        try {
            Field field = DynamicTexture.class.getDeclaredField(MCPNames.field("field_110566_b"));
            field.setAccessible(true);
            dynTexDataGet = MethodHandles.publicLookup().unreflectGetter(field);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

	public static void updateDynamicTexture(DynamicTexture texture, BufferedImage image) {
        int[] textureData;
        try {
            textureData = (int[]) dynTexDataGet.invokeExact(texture);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), textureData, 0, image.getWidth());
        texture.updateDynamicTexture();
    }

}
