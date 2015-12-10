package de.take_weiland.mods.cameracraft.client.gui.memory.handler;

import de.take_weiland.mods.cameracraft.api.photo.PhotoStorageRenamable;
import de.take_weiland.mods.cameracraft.item.ItemPhotoStorages;
import net.minecraft.item.ItemStack;

/**
 * @author Intektor
 */
public class ImageFile {

    protected long photoID;
    protected int position;
    private final int index;

    public ImageFile(long photoID, int index) {
        this.photoID = photoID;
        this.index = index;
    }

    public int getPosition() {
        return position;
    }

    public int getIndex() {
        return index;
    }

    public String getPhotoName(ItemStack stack) {
        ItemPhotoStorages itemStorage = (ItemPhotoStorages) stack.getItem();
        PhotoStorageRenamable storage = (PhotoStorageRenamable) itemStorage.getPhotoStorage(stack);
        return storage.getName(getIndex());
    }
}
