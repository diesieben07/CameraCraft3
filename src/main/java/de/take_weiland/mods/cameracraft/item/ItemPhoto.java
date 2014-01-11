package de.take_weiland.mods.cameracraft.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.google.common.collect.ImmutableList;

import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.cameracraft.api.photo.PhotoStorage;
import de.take_weiland.mods.cameracraft.api.photo.PhotoStorageItem;
import de.take_weiland.mods.cameracraft.photo.AbstractPhotoStorage;
import de.take_weiland.mods.cameracraft.photo.PhotoManager;
import de.take_weiland.mods.commons.util.Sides;

public class ItemPhoto extends CCItemMultitype<PhotoType> implements PhotoStorageItem {

	public static final String NBT_KEY = "cameracraft.photoId";

	public ItemPhoto(int defaultId) {
		super("photo", defaultId);
		setMaxStackSize(1);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List info, boolean enhanced) {
		super.addInformation(stack, player, info, enhanced);
		if (stack.hasTagCompound()) {
			info.add(stack.getTagCompound().getString(NBT_KEY));
		}
	}

	@Override
	protected List<ItemStack> provideSubtypes() {
		return ImmutableList.of();
	}

	@Override
	public PhotoType[] getTypes() {
		return PhotoType.VALUES;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (Sides.logical(world).isClient() && stack.hasTagCompound()) {
			CameraCraft.env.displayPhotoGui(stack.getTagCompound().getString(NBT_KEY));
		}
		return stack;
	}

	@Override
	public PhotoStorage getPhotoStorage(final ItemStack stack) {
		if (!stack.hasTagCompound()) {
			return null;
		}
		
		return new AbstractPhotoStorage(true) {
			
			private final String photoId = PhotoManager.asString(stack.getTagCompound().getInteger(NBT_KEY));
			private final List<String> contents = ImmutableList.of(photoId); 
			
			@Override
			public int size() {
				return 1;
			}
			
			@Override
			public List<String> getPhotos() {
				return contents;
			}
			
			@Override
			public int capacity() {
				return 1;
			}
			
			@Override
			protected String getImpl(int index) {
				return photoId;
			}
			
			private int[] raw;
			
			@Override
			public int[] getRawPhotoIds() {
				return raw == null ? (raw = new int[] { PhotoManager.asInt(photoId) }) : raw;
			}
			
			// photos are immutable
			@Override
			protected void storeImpl(String photoId) { }
			
			@Override
			protected void removeImpl(int index) { }
			
			@Override
			protected void clearImpl() { }

		};
	}

	@Override
	public boolean isSealed(ItemStack stack) {
		return true;
	}

	@Override
	public ItemStack unseal(ItemStack sealed) {
		return sealed;
	}

	@Override
	public boolean canRewind(ItemStack stack) {
		return false;
	}

	@Override
	public ItemStack rewind(ItemStack stack) {
		return stack;
	}

	@Override
	public boolean canBeProcessed(ItemStack stack) {
		return false;
	}

	@Override
	public ItemStack process(ItemStack stack) {
		return stack;
	}

	@Override
	public boolean isScannable(ItemStack stack) {
		return false;
	}

}
