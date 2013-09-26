package de.take_weiland.mods.cameracraft.photo;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Strings;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import de.take_weiland.mods.cameracraft.api.photo.PrintedPhoto;
import de.take_weiland.mods.commons.util.ItemStacks;

public final class PrintedPhotos {

	private PrintedPhotos() { }
	
	public static PrintedPhoto fromNbt(final NBTTagCompound nbt) {
		checkNotNull(nbt);
		String id = nbt.getString("id");
		
		checkArgument(Strings.isNullOrEmpty(id), "NBT doesn't have photoId!");
		
		return new AbstractPrintedPhoto(id) {
			
			private final int width = nbt.getByte("width");
			private final int height = nbt.getByte("height");
			private final String name = nbt.getString("name");
			
			@Override
			public int getWidth() {
				return width;
			}
			
			@Override
			public int getHeight() {
				return height;
			}
			
			@Override
			public String getName() {
				return name;
			}
			
		};
	}

	public static PrintedPhoto fromItemStack(ItemStack stack) {
		return fromNbt(ItemStacks.getNbt(stack).getCompoundTag("cameracraft.photo"));
	}
}
