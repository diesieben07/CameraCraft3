package de.take_weiland.mods.cameracraft.photo;

import de.take_weiland.mods.cameracraft.CCWorldData;
import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.cameracraft.api.img.ImageFilter;
import gnu.trove.list.TLongList;
import net.minecraft.world.World;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public final class PhotoManager {

	private PhotoManager() { }
	
	public static final int PHOTO_SIZE = 256;
	
	public static long nextPhotoId(World world) {
		return CCWorldData.get(world).nextId();
	}

	public static CompletableFuture<?> applyFilterTo(TLongList photoIds, final ImageFilter filter) {
        int len = photoIds.size();
        CompletableFuture<?>[] result = new CompletableFuture<?>[len];

        for (int i = 0; i < len; i++) {
            long photoId = photoIds.get(i);
            File file = DatabaseImpl.instance.getImageFile(photoId);

            result[i] = CompletableFuture.supplyAsync(() -> readImage(file))
                    .thenApplyAsync(filter::apply)
                    .thenAcceptAsync(image -> writeImage(image, file))
                    .whenCompleteAsync((v, x) -> {
                        if (x != null) {
                            CameraCraft.logger.error("Photo processing failed!", x);
                        }
                    });
        }
		return CompletableFuture.allOf(result);
	}

    private static BufferedImage readImage(File file) {
        try {
            return ImageIO.read(file);
        } catch (IOException e) {
            throw new CompletionException(e);
        }
    }

    private static void writeImage(BufferedImage image, File file) {
        try {
            ImageIO.write(image, "PNG", file);
        } catch (IOException e) {
            throw new CompletionException(e);
        }
    }

}
