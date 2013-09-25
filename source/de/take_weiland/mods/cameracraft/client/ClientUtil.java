package de.take_weiland.mods.cameracraft.client;

import java.nio.ByteBuffer;

import net.minecraft.client.Minecraft;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

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

}
