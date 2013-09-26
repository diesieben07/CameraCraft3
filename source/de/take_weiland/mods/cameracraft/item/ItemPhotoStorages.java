package de.take_weiland.mods.cameracraft.item;

import static de.take_weiland.mods.cameracraft.item.PhotoStorageType.FILM_B_W;
import static de.take_weiland.mods.cameracraft.item.PhotoStorageType.FILM_COLOR;
import static de.take_weiland.mods.cameracraft.item.PhotoStorageType.MEMORY_CARD;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import de.take_weiland.mods.cameracraft.api.camera.ItemPhotoStorage;
import de.take_weiland.mods.cameracraft.api.camera.PhotoStorage;
import de.take_weiland.mods.cameracraft.photo.PhotoStorages;
import de.take_weiland.mods.commons.util.Multitypes;

public class ItemPhotoStorages extends CCItemMultitype<PhotoStorageType> implements ItemPhotoStorage {

	public ItemPhotoStorages(int defaultId) {
		super("photoStorage", defaultId);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List text, boolean verbose) {
		int cap = Multitypes.getType(this, stack).getCapacity();
		int size = PhotoStorages.fastSize(stack);
		text.add(size + " / " + cap);
	}

	@Override
	public PhotoStorageType[] getTypes() {
		return PhotoStorageType.VALUES;
	}

	@Override
	protected List<ItemStack> provideSubtypes() {
		return Multitypes.stacks(FILM_B_W, FILM_COLOR, MEMORY_CARD);
	}

	@Override
	public PhotoStorage getStorage(ItemStack stack) {
		return Multitypes.getType(this, stack).getStorage(stack);
	}


}
