package de.take_weiland.mods.cameracraft.photo;

import java.util.List;

import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import de.take_weiland.mods.cameracraft.api.camera.PhotoStorage;
import de.take_weiland.mods.commons.util.NBT;

public final class PhotoStorages {

	private PhotoStorages() { }
	
	public static PhotoStorage withCapacity(final int cap, boolean sealed, NBTTagList nbt) {
		final List<NBTTagString> list = NBT.asList(nbt);
		return new AbstractPhotoStorage(sealed) {
			
			@Override
			public int size() {
				return list.size();
			}
			
			@Override
			public int capacity() {
				return cap;
			}
			
			@Override
			public void remove(int index) {
				checkNotSealed();
				list.remove(index);
			}

			@Override
			protected String getImpl(int index) {
				return list.get(index).data;
			}

			@Override
			protected void storeImpl(String photoId) {
				list.add(new NBTTagString("", photoId));
			}
		};
	}
	
}
