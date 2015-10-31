package de.take_weiland.mods.cameracraft.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.take_weiland.mods.cameracraft.api.photo.PhotoStorage;
import de.take_weiland.mods.cameracraft.api.photo.PhotoStorageItem;
import de.take_weiland.mods.cameracraft.photo.PhotoStorages;
import de.take_weiland.mods.commons.client.I18n;
import de.take_weiland.mods.commons.meta.MetadataProperty;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemPhotoStorages extends CCItemMultitype<PhotoStorageType> implements PhotoStorageItem {

    private static final MetadataProperty<PhotoStorageType> typeProp = MetadataProperty.newProperty(0, PhotoStorageType.class);

	public ItemPhotoStorages() {
		super("photoStorage");
		setMaxStackSize(1);
	}

    @Override
    public MetadataProperty<PhotoStorageType> subtypeProperty() {
        return typeProp;
    }

    @SuppressWarnings({ "rawtypes", "unchecked", "boxing" })
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List text, boolean verbose) {
		PhotoStorageType type = getType(stack);
		String key = type.isSealed() ? "item.CameraCraft.photoStorage.subtext" : "item.CameraCraft.photoStorage.subtext.sealed";
		int size = PhotoStorages.fastSize(stack);
		if (size == 1) {
			key += ".single";
		}
		text.add(I18n.translate(key, size, type.getCapacity()));
	}

	@Override
	public PhotoStorage getPhotoStorage(ItemStack stack) {
		return getType(stack).getStorage(stack);
	}

	@Override
	public boolean isSealed(ItemStack stack) {
		return getType(stack).isSealed();
	}

	@Override
	public ItemStack unseal(ItemStack sealed) {
		return applyType(sealed, getType(sealed).getUnsealed());
	}
	
	@Override
	public boolean canRewind(ItemStack stack) {
		return getType(stack).canRewind();
	}

	@Override
	public ItemStack rewind(ItemStack stack) {
		return applyType(stack, getType(stack).rewind());
	}
	
	@Override
	public boolean canBeProcessed(ItemStack stack) {
		return getType(stack).canProcess();
	}

	@Override
	public ItemStack process(ItemStack stack) {
		return applyType(stack, getType(stack).getProcessed());
	}
	
	@Override
	public boolean canBeScanned(ItemStack stack) {
		return getType(stack).isScannable();
	}

	@Override
	public boolean isRandomAccess(ItemStack stack) {
		return getType(stack) == PhotoStorageType.MEMORY_CARD;
	}

	private static ItemStack applyType(ItemStack stack, PhotoStorageType type) {
		ItemStack result = stack.copy();
        result.setMetadata(typeProp.toMeta(type));
		return result;
	}

}
