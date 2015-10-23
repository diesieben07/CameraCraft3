package de.take_weiland.mods.cameracraft.api.photo;

import de.take_weiland.mods.cameracraft.api.img.ImageFilter;
import gnu.trove.set.TLongSet;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author diesieben07
 */
public interface PhotoDatabase {

    TLongSet getPhotoIDs();

    Iterable<Photo> getPhotos();

    Photo getPhoto(long id);

    default void saveImage(long id, BufferedImage image) throws IOException {
        saveImage(id, image, null);
    }

    void saveImage(long id, BufferedImage image, ImageFilter filter) throws IOException;

    void applyFilter(long id, ImageFilter filter) throws IOException;

    long nextId();

    void store(long photoID, Photo data);

}