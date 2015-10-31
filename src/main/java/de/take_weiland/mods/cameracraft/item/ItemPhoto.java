package de.take_weiland.mods.cameracraft.item;

import com.google.common.base.Strings;
import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.cameracraft.api.photo.PhotoItem;
import de.take_weiland.mods.cameracraft.api.photo.PhotoStorage;
import de.take_weiland.mods.cameracraft.entity.EntityPoster;
import de.take_weiland.mods.cameracraft.photo.SinglePhotoStorage;
import de.take_weiland.mods.commons.meta.MetadataProperty;
import de.take_weiland.mods.commons.util.ItemStacks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Direction;
import net.minecraft.world.World;

import static de.take_weiland.mods.commons.util.Sides.sideOf;

public class ItemPhoto extends CCItemMultitype<PhotoType> implements PhotoItem {

    private static final MetadataProperty<PhotoType> types = MetadataProperty.newProperty(0, PhotoType.class);

    private static final String NBT_KEY_BASE = CameraCraft.MOD_ID;
	public static final String NBT_KEY_ID = "photo";
	private static final String NBT_KEY_NAME = "name";
    private static final String NBT_KEY_WIDTH = "width";
    private static final String NBT_KEY_HEIGHT = "height";

	public ItemPhoto() {
		super("photo");
		setMaxStackSize(1);
	}

    @Override
    public MetadataProperty<PhotoType> subtypeProperty() {
        return types;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        if (isNamed(stack)) {
            return getNameImpl(stack);
        }
        return super.getItemStackDisplayName(stack);
    }

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (sideOf(world).isClient() && stack.hasTagCompound()) {
			boolean isNamed = isNamed(stack);
			CameraCraft.proxy.displayPhotoGui(getPhotoId(stack), isNamed ? getNameImpl(stack) : null, !isNamed);
		}
		return stack;
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if (getType(stack) != PhotoType.POSTER || !stack.hasTagCompound() || side < 2 || !player.canPlayerEdit(x, y, z, side, stack)) {
            return false;
        }

		EntityPoster poster = new EntityPoster(world, x, y, z, Direction.facingToDirection[side], stack, 4, 4);
		if (poster.onValidSurface()) {
			if (sideOf(world).isServer()) {
				world.spawnEntityInWorld(poster);
			}
			player.destroyCurrentEquippedItem();
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public long getPhotoId(final ItemStack stack) {
        return getNbt(stack).getLong(NBT_KEY_ID);
	}
	
	@Override
	public void setPhotoId(ItemStack stack, long photoId) {
		getNbt(stack).setLong(NBT_KEY_ID, photoId);
	}

	@Override
	public Size getSize(ItemStack stack) {
		NBTTagCompound nbt = getNbt(stack);
        return new PhotoItem.Size(nbt.getInteger(NBT_KEY_WIDTH), nbt.getInteger(NBT_KEY_HEIGHT));
	}

    @Override
    public void setSize(ItemStack stack, Size size) {
        NBTTagCompound nbt = getNbt(stack);
        nbt.setInteger(NBT_KEY_WIDTH, size.getWidth());
        nbt.setInteger(NBT_KEY_HEIGHT, size.getHeight());
    }

    @Override
	public PhotoStorage getPhotoStorage(ItemStack stack) {
		if (!stack.hasTagCompound()) {
			return null;
		}
		
		return new SinglePhotoStorage(getPhotoId(stack));
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
	public boolean canBeScanned(ItemStack stack) {
		return false;
	}

	@Override
	public boolean isRandomAccess(ItemStack stack) {
		return false;
	}

	@Override
	public boolean isNamed(ItemStack stack) {
		return getNbt(stack).hasKey(NBT_KEY_NAME);
	}

	@Override
	public String getName(ItemStack stack) {
		return isNamed(stack) ? getNameImpl(stack) : null;
	}

	private String getNameImpl(ItemStack stack) {
		return getNbt(stack).getString(NBT_KEY_NAME);
	}

	@Override
	public void setName(ItemStack stack, String name) {
		if (!isNamed(stack)) {
			getNbt(stack).setString(NBT_KEY_NAME, Strings.nullToEmpty(name));
		}
	}

    private static NBTTagCompound getNbt(ItemStack stack) {
        return ItemStacks.getNbt(stack, NBT_KEY_BASE);
    }

}
