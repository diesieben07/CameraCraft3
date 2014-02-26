package de.take_weiland.mods.cameracraft.photo;

import com.google.common.primitives.Ints;
import de.take_weiland.mods.cameracraft.api.img.ImageFilter;
import de.take_weiland.mods.cameracraft.api.photo.PhotoStorage;
import de.take_weiland.mods.commons.util.ItemStacks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class PhotoStorages {

	private static final String NBT_KEY = "cameracraft.photos";

	private PhotoStorages() { }
	
	public static int fastSize(ItemStack stack) {
		return ItemStacks.getNbt(stack).getIntArray(NBT_KEY).length;
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
			int newLen = nbt.intArray.length - 1;
			if (newLen == 0) {
				nbt.intArray = ArrayUtils.EMPTY_INT_ARRAY;
			} else {
				int[] arr = new int[newLen];
				System.arraycopy(nbt.intArray, 0, arr, 0, index);
				if (index != newLen) {
					System.arraycopy(nbt.intArray, index + 1, arr, index, newLen - index);
				}
				nbt.intArray = arr;
			}
		}

		@Override
		protected Integer getImpl(int index) {
			return Integer.valueOf(nbt.intArray[index]);
		}

		@Override
		protected void storeImpl(Integer photoId) {
			int len = nbt.intArray.length;
			nbt.intArray = Arrays.copyOf(nbt.intArray, len + 1);
			nbt.intArray[len] = photoId.intValue();

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

		private List<Integer> listView;
		
		@Override
		public List<Integer> getPhotos() {
			return listView == null ? (listView = createListView()) : listView;
		}

		private List<Integer> createListView() {
			return Collections.unmodifiableList(Ints.asList(nbt.intArray));
		}
	}

}
