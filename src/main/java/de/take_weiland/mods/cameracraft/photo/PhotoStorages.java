package de.take_weiland.mods.cameracraft.photo;

import java.util.AbstractList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;

import org.apache.commons.lang3.ArrayUtils;

import de.take_weiland.mods.cameracraft.api.img.ImageFilter;
import de.take_weiland.mods.cameracraft.api.photo.PhotoStorage;
import de.take_weiland.mods.commons.util.ItemStacks;

public final class PhotoStorages {

	private static final String NBT_KEY = "cameracraft.photos";

	private PhotoStorages() { }
	
	public static int fastSize(ItemStack stack) {
		return ItemStacks.getNbt(stack).getTagList(NBT_KEY).tagCount();
	}
	
	public static PhotoStorage withCapacity(int cap, boolean sealed, ItemStack stack) {
		return withCapacity(cap, sealed, stack, null);
	}
	
	public static PhotoStorage withCapacity(int cap, boolean sealed, ItemStack stack, ImageFilter filter) {
		NBTTagCompound stackNbt = ItemStacks.getNbt(stack);
		if (!stackNbt.hasKey(NBT_KEY)) {
			stackNbt.setIntArray(NBT_KEY, ArrayUtils.EMPTY_INT_ARRAY);
		}
		NBTTagIntArray nbt = (NBTTagIntArray) ItemStacks.getNbt(stack).getTag(NBT_KEY);
		return withCapacity(cap, sealed, nbt, filter);
	}
	
	public static PhotoStorage withCapacity(final int cap, boolean sealed, final NBTTagIntArray nbt) {
		return withCapacity(cap, sealed, nbt, null);
	}
	
	public static PhotoStorage withCapacity(final int cap, boolean sealed, final NBTTagIntArray nbt, final ImageFilter filter) {
		return new ItemStackPhotoStorage(sealed, cap, nbt, filter);
	}
	
	private static final class ItemStackPhotoStorage extends AbstractPhotoStorage {
		
		private final int cap;
		private final NBTTagIntArray nbt;
		private final ImageFilter filter;

		ItemStackPhotoStorage(boolean isSealed, int cap, NBTTagIntArray nbt, ImageFilter filter) {
			super(isSealed);
			this.cap = cap;
			this.nbt = nbt;
			this.filter = filter;
		}

		@Override
		public int size() {
			return nbt.intArray.length;
		}

		@Override
		public int capacity() {
			return cap;
		}

		@Override
		protected void removeImpl(int index) {
			nbt.intArray = ArrayUtils.remove(nbt.intArray, index);
		}

		@Override
		protected String getImpl(int index) {
			return PhotoManager.asString(nbt.intArray[index]);
		}

		@Override
		protected void storeImpl(String photoId) {
			nbt.intArray = ArrayUtils.add(nbt.intArray, PhotoManager.asInt(photoId));
		}

		@Override
		protected void clearImpl() {
			nbt.intArray = ArrayUtils.EMPTY_INT_ARRAY;
		}

		@Override
		public ImageFilter getFilter() {
			return filter;
		}
		

		@Override
		public int[] getRawPhotoIds() {
			return nbt.intArray;
		}

		private List<String> listView;
		
		@Override
		public List<String> getPhotos() {
			return listView == null ? (listView = createListView()) : listView;
		}
		
		private List<String> createListView() {
			return new AbstractList<String>() {

				@Override
				public String get(int index) {
					return ItemStackPhotoStorage.this.get(index);
				}

				@Override
				public int size() {
					return ItemStackPhotoStorage.this.size();
				}
			};
		}
	}

}
