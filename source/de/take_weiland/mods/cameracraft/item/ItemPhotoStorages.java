package de.take_weiland.mods.cameracraft.item;

import static de.take_weiland.mods.cameracraft.item.PhotoStorageType.FILM_B_W;
import static de.take_weiland.mods.cameracraft.item.PhotoStorageType.FILM_COLOR;
import static de.take_weiland.mods.cameracraft.item.PhotoStorageType.MEMORY_CARD;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import de.take_weiland.mods.cameracraft.api.photo.PhotoStorageItem;
import de.take_weiland.mods.cameracraft.api.photo.PhotoStorage;
import de.take_weiland.mods.cameracraft.photo.PhotoStorages;
import de.take_weiland.mods.commons.util.ItemStacks;
import de.take_weiland.mods.commons.util.Multitypes;

public class ItemPhotoStorages extends CCItemMultitype<PhotoStorageType> implements PhotoStorageItem {

	public ItemPhotoStorages(int defaultId) {
		super("photoStorage", defaultId);
	}

	@SuppressWarnings({ "rawtypes", "unchecked", "boxing" })
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List text, boolean verbose) {
		PhotoStorageType type = Multitypes.getType(this, stack);
		String key = type.isSealed() ? "item.CameraCraft.photoStorage.subtext" : "item.CameraCraft.photoStorage.subtext.sealed";
		text.add(I18n.getStringParams(key, PhotoStorages.fastSize(stack), type.getCapacity()));
	}

	@Override
	public PhotoStorageType[] getTypes() {
		return PhotoStorageType.VALUES;
	}

	@Override
	protected List<ItemStack> provideSubtypes() {
		return ItemStacks.of(FILM_B_W, FILM_COLOR, MEMORY_CARD);
	}

	@Override
	public PhotoStorage getStorage(ItemStack stack) {
		return Multitypes.getType(this, stack).getStorage(stack);
	}


}
