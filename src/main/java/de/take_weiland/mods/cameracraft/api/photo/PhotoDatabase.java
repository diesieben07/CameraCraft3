package de.take_weiland.mods.cameracraft.api.photo;

import de.take_weiland.mods.cameracraft.api.CameraCraftApi;
import de.take_weiland.mods.cameracraft.api.img.ImageFilter;
import gnu.trove.iterator.TLongIterator;
import gnu.trove.list.TLongList;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.image.BufferedImage;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;

/**
 * <p>Central database for photos. Obtain via {@link CameraCraftApi#getDatabase()}.</p>
 *
 * @author diesieben07
 */
@ParametersAreNonnullByDefault
public interface PhotoDatabase {

    Iterable<Photo> getPhotos();

    TLongList ids();

    TLongIterator idIterator();

    CompletionStage<Photo> getPhotoAsync(long id);

    Photo getPhoto(long id) throws CompletionException;

    /**
     * <p>Set the data for the given photo ID.</p>
     *
     * @param photoID the photo ID
     * @param data    the data
     */
    CompletionStage<Photo> setData(long photoID, PhotoData data);

    /**
     * <p>Get the given image asynchronously. The returned stage will complete normally with the loaded image when the
     * image finishes loading and will complete exceptionally when the image fails to load.</p>
     *
     * @param id the photoId
     * @return a CompletionStage
     */
    CompletionStage<BufferedImage> getImageAsync(long id);

    /**
     * <p>Get the given image synchronously. If the image is not yet loaded this method will block until any necessary
     * IO is completed.</p>
     * <p>This method is equivalent to {@code getImageAsync(id).toCompletableFuture().join()} but may be more efficient.</p>
     *
     * @param id the photoId
     * @return a BufferedImage
     * @throws CompletionException if the loading completes exceptionally
     */
    BufferedImage getImage(long id) throws CompletionException;

    /**
     * <p>Claim a new photo ID and save the given image with that ID. The returned stage will complete normally with the
     * new ID when the image was successfully saved and will complete exceptionally when an error occurs while writing the
     * image. In case of an error an ID is still claimed, but will be invalid.</p>
     *
     * @param image the image
     * @return a CompletionStage
     */
    default CompletionStage<Long> saveNewImage(BufferedImage image) {
        return saveNewImage(image, null);
    }

    /**
     * <p>Claim a new photo ID and save the given image with that ID after applying the given filter, if any. The returned
     * stage will complete normally with the new ID when the image was successfully saved and will complete exceptionally
     * when an error occurs while writing the image. In case of an error an ID is still claimed, but will be invalid.</p>
     *
     * @param image the image
     * @return a CompletionStage
     */
    CompletionStage<Long> saveNewImage(BufferedImage image, @Nullable ImageFilter filter);

    /**
     * <p>Apply the given filter to the image with the given ID. The returned stage will complete normally when the filter
     * was successfully applied and will complete exceptionally if an error occurs.</p>
     *
     * @param id     the photo ID
     * @param filter the image filter
     * @return a CompletionStage
     */
    CompletionStage<BufferedImage> applyFilter(long id, ImageFilter filter);

    /**
     * <p>Overwrite the given ID with the given image. The returned stage will complete normally when the image was successfully
     * written and will complete exceptionally if an error occurs.</p>
     *
     * @param id    the photo ID
     * @param image the image
     * @return a CompletionStage
     */
    CompletionStage<BufferedImage> setImage(long id, BufferedImage image);

    /**
     * <p>Claim a new Photo ID.</p>
     *
     * @return a new photo ID
     */
    long nextId();

}