package de.take_weiland.mods.cameracraft.api.photo;

/**
 * @author Intektor
 */
public interface NamedPhotoStorage extends PhotoStorage {

    String getName(int index);

    void setName(int index, String name);

}
