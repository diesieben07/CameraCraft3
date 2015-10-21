package de.take_weiland.mods.cameracraft.photo;

import com.google.common.base.Throwables;
import com.google.common.primitives.Longs;
import de.take_weiland.mods.cameracraft.api.img.ImageFilter;
import de.take_weiland.mods.cameracraft.api.photo.PhotoStorage;
import de.take_weiland.mods.commons.asm.MCPNames;
import de.take_weiland.mods.commons.nbt.NBT;
import de.take_weiland.mods.commons.util.ItemStacks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.Arrays;

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
        NBTTagByteArray nbt = NBT.getOrCreate(stackNbt, NBT_KEY, NBTTagByteArray.class);
		return withCapacity(cap, sealed, nbt, filter);
	}
	
	public static PhotoStorage withCapacity(final int cap, boolean sealed, final NBTTagByteArray nbt) {
		return withCapacity(cap, sealed, nbt, null);
	}
	
	public static PhotoStorage withCapacity(final int cap, boolean sealed, final NBTTagByteArray nbt, final ImageFilter filter) {
		return new ItemStackPhotoStorage(sealed, cap, nbt, filter);
	}
	
	private static final class ItemStackPhotoStorage extends AbstractPhotoStorage {

        private final boolean isSealed;
        private final int cap;
		private final NBTTagByteArray nbt;
		private final ImageFilter filter;

		ItemStackPhotoStorage(boolean isSealed, int cap, NBTTagByteArray nbt, ImageFilter filter) {
			super();
            this.isSealed = isSealed;
            this.cap = cap;
			this.nbt = nbt;
			this.filter = filter;
		}

        @Override
		public int size() {
			return nbt.func_150292_c().length >> 3;
		}

		@Override
		public int capacity() {
			return cap;
		}

		@Override
		protected void removeImpl(int index) {
			int newLen = size() - 1;
			if (newLen == 0) {
                clearImpl();
            } else {
				byte[] newArr = new byte[toArrayIndex(newLen)];
                byte[] oldArr = nbt.func_150292_c();

                System.arraycopy(oldArr, 0, newArr, 0, toArrayIndex(index));
				if (index != newLen) { // not last was removed
					System.arraycopy(oldArr, toArrayIndex(index + 1), newArr, toArrayIndex(index), toArrayIndex(newLen - index));
				}
				setNbtByteArray(nbt, newArr);
			}
		}

        private static int toArrayIndex(int i) {
            return i << 3;
        }

		@Override
		protected long getImpl(int index) {
			int arrIdx = toArrayIndex(index);
            byte[] arr = nbt.func_150292_c();
            return Longs.fromBytes(arr[arrIdx], arr[arrIdx + 1], arr[arrIdx + 2], arr[arrIdx + 3],
                    arr[arrIdx + 4], arr[arrIdx + 5], arr[arrIdx + 6], arr[arrIdx + 7]);
        }

		@Override
		protected void storeImpl(long photoId) {
            byte[] newArr = concat(nbt.func_150292_c(), Longs.toByteArray(photoId));
            setNbtByteArray(nbt, newArr);
		}

        private static byte[] concat(byte[] a, byte[] b) {
            int aLen = a.length;
            int bLen = b.length;
            byte[] result = Arrays.copyOf(a, aLen + bLen);
            System.arraycopy(b, 0, a, aLen, bLen);
            return result;
        }

		@Override
		protected void clearImpl() {
			setNbtByteArray(nbt, ArrayUtils.EMPTY_BYTE_ARRAY);
		}

		@Override
		public ImageFilter getFilter() {
			return filter;
		}

        @Override
        public boolean isSealed() {
            return isSealed;
        }
    }

    static void setNbtByteArray(NBTTagByteArray nbt, byte[] arr) {
        try {
            nbtByteArraySet.invokeExact(nbt, arr);
        } catch (Throwable t) {
            throw Throwables.propagate(t);
        }
    }

	private static final MethodHandle nbtByteArraySet;

    static {
        try {
            Field field = NBTTagByteArray.class.getDeclaredField(MCPNames.field("field_74754_a"));
            field.setAccessible(true);
            nbtByteArraySet = MethodHandles.publicLookup().unreflectSetter(field);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }

    }

}
