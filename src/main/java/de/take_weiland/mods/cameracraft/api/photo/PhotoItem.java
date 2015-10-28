package de.take_weiland.mods.cameracraft.api.photo;

import net.minecraft.item.ItemStack;

/**
 * <p>An Item that represents a single photo.</p>
 */
public interface PhotoItem extends PhotoStorageItem {

	boolean isNamed(ItemStack stack);
	
	String getName(ItemStack stack);
	
	void setName(ItemStack stack, String name);

	long getPhotoId(ItemStack stack);
	
	void setPhotoId(ItemStack stack, long photoId);

    Size getSize(ItemStack stack);

    void setSize(ItemStack stack, Size size);

    final class Size {

        private final int width;
        private final int height;

        public Size(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }

}
