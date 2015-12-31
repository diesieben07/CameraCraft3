package de.take_weiland.mods.cameracraft.api.photo;

import java.awt.image.BufferedImage;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;

/**
 * <p>Represents a Photo.</p>
 *
 * @author diesieben07
 */
public interface Photo extends PhotoData {

    /**
     * <p>The Photo ID.</p>
     *
     * @return the ID
     */
    long getId();

    /**
     * <p>Get the associated image. This is equivalent to {@link PhotoDatabase#getImage(long)}.</p>
     *
     * @return the BufferedImage
     */
    BufferedImage getImage() throws CompletionException;

    /**
     * <p>Get the associated image asynchronously. This method is equivalent to {@link PhotoDatabase#getImageAsync(long)}.</p>
     *
     * @return a CompletionStage
     */
    CompletionStage<BufferedImage> getImageAsync();

}
