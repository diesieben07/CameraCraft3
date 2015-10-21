package de.take_weiland.mods.cameracraft.api.photo;

import gnu.trove.set.TLongSet;

/**
 * @author diesieben07
 */
public interface PhotoDatabase {

    TLongSet getPhotoIDs();

    Iterable<Photo> getPhotos();

    Photo getPhoto(long id);

    long nextId();

    void store(long photoID, Photo data);

}