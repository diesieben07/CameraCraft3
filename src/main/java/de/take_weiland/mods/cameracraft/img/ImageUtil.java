package de.take_weiland.mods.cameracraft.img;

import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.cameracraft.api.img.ImageFilter;
import de.take_weiland.mods.cameracraft.api.photo.PhotoStorage;
import net.minecraft.util.MathHelper;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public final class ImageUtil {

    private ImageUtil() {
    }

    public static BufferedImage fromRawRotatedRgb(int width, int height, byte[] data) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int i = (x + width * y) * 3;
                int r = data[i] & 0xFF;
                int g = data[i + 1] & 0xFF;
                int b = data[i + 2] & 0xFF;
                    image.setRGB(x, height - y - 1, 0xFF << 24 | r << 16 | g << 8 | b);
            }
        }
        return image;
    }

    public static int clampRgb(int value) {
        return MathHelper.clamp_int(value, 0, 255);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static CompletionStage<Void>[] applyFilter(PhotoStorage storage, ImageFilter filter) {
        int len = storage.size();
        CompletableFuture<Void>[] futures = new CompletableFuture[len];

        for (int i = 0; i < len; i++) {
            long photoId = storage.get(i);
            // TODO no need for async here!
            futures[i] = CompletableFuture.runAsync(() -> CameraCraft.currentDatabase().applyFilter(photoId, filter));
        }

        return futures;
    }

    public static void resetImage(BufferedImage image) {
        int[] nullnull = new int[image.getHeight() * image.getWidth()];
        image.setRGB(0, 0, image.getWidth(), image.getHeight(), nullnull, 0, image.getWidth());
    }

    // http://stackoverflow.com/a/3514297
    public static BufferedImage clone(BufferedImage img) {
        ColorModel cm = img.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = img.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
}
