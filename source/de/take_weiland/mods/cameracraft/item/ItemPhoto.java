package de.take_weiland.mods.cameracraft.item;

import java.util.List;

import net.minecraft.item.ItemStack;

import com.google.common.collect.ImmutableList;

public class ItemPhoto extends CCItemMultitype<PhotoType> {

	public ItemPhoto(int defaultId) {
		super("photo", defaultId);
	}

	@Override
	protected List<ItemStack> provideSubtypes() {
		return ImmutableList.of();
	}

	@Override
	public PhotoType[] getTypes() {
		return PhotoType.VALUES;
	}

}
