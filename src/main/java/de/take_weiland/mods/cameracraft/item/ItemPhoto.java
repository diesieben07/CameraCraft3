package de.take_weiland.mods.cameracraft.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.world.World;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;

import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.cameracraft.api.photo.PhotoItem;
import de.take_weiland.mods.cameracraft.api.photo.PhotoStorage;
import de.take_weiland.mods.cameracraft.entity.EntityPoster;
import de.take_weiland.mods.cameracraft.photo.AbstractPhotoStorage;
import de.take_weiland.mods.cameracraft.photo.PhotoManager;
import de.take_weiland.mods.commons.util.ItemStacks;
import de.take_weiland.mods.commons.util.Multitypes;
import de.take_weiland.mods.commons.util.Sides;

public class ItemPhoto extends CCItemMultitype<PhotoType> implements PhotoItem {

	public static final String NBT_KEY = "cameracraft.photoId";
	private static final String NBT_NAME_KEY = "cameracraft.photoname";

	public ItemPhoto(int defaultId) {
		super("photo", defaultId);
		setMaxStackSize(1);
	}

	@Override
	public String getItemDisplayName(ItemStack stack) {
		if (isNamed(stack)) {
			return getNameImpl(stack);
		}
		return super.getItemDisplayName(stack);
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
			boolean isNamed = isNamed(stack);
			CameraCraft.env.displayPhotoGui(getPhotoId(stack), isNamed ? getNameImpl(stack) : null, !isNamed);
		}
		return stack;
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if (Multitypes.getType(this, stack) != PhotoType.POSTER || !stack.hasTagCompound() || side < 2 || !player.canPlayerEdit(x, y, z, side, stack)) {
			return false;
		}
		EntityPoster poster = new EntityPoster(world, x, y, z, Direction.facingToDirection[side], stack);
		if (poster.onValidSurface()) {
			if (Sides.logical(world).isServer()) {
				world.spawnEntityInWorld(poster);
			}
			player.destroyCurrentEquippedItem();
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public Integer getPhotoId(final ItemStack stack) {
		return Integer.valueOf(ItemStacks.getNbt(stack).getInteger(NBT_KEY));
	}
	
	@Override
	public void setPhotoId(ItemStack stack, Integer photoId) {
		ItemStacks.getNbt(stack).setInteger(NBT_KEY, photoId.intValue());
	}

	@Override
	public PhotoStorage getPhotoStorage(final ItemStack stack) {
		if (!stack.hasTagCompound()) {
			return null;
		}
		
		return new AbstractPhotoStorage(true) {
			
			private final Integer photoId = getPhotoId(stack);
			private final List<Integer> contents = ImmutableList.of(photoId);
			
			@Override
			public int size() {
				return 1;
			}
			
			@Override
			public List<Integer> getPhotos() {
				return contents;
			}
			
			@Override
			public int capacity() {
				return 1;
			}
			
			@Override
			protected Integer getImpl(int index) {
				return photoId;
			}
			
			private int[] raw;
			
			@Override
			public int[] getRawPhotoIds() {
				return raw == null ? (raw = new int[] { photoId.intValue() }) : raw;
			}
			
			// photos are immutable
			@Override
			protected void storeImpl(Integer photoId) { }
			
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

	@Override
	public boolean isNamed(ItemStack stack) {
		return ItemStacks.getNbt(stack).hasKey(NBT_NAME_KEY);
	}

	@Override
	public String getName(ItemStack stack) {
		return isNamed(stack) ? getNameImpl(stack) : null;
	}

	private String getNameImpl(ItemStack stack) {
		return ItemStacks.getNbt(stack).getString(NBT_NAME_KEY);
	}

	@Override
	public void setName(ItemStack stack, String name) {
		if (!isNamed(stack)) {
			ItemStacks.getNbt(stack).setString(NBT_NAME_KEY, Strings.nullToEmpty(name));
		}
	}

}
