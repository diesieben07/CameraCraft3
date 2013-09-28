package de.take_weiland.mods.cameracraft.item;

import net.minecraft.item.ItemStack;
import de.take_weiland.mods.cameracraft.api.camera.Lens;
import de.take_weiland.mods.cameracraft.api.img.ImageFilter;
import de.take_weiland.mods.commons.util.Multitypes;

public class ItemLens extends CCItemMultitype<LensType> implements Lens {

	public ItemLens(int defaultId) {
		super("lens", defaultId);
	}

	@Override
	public LensType[] getTypes() {
		return LensType.VALUES;
	}

	@Override
	public ImageFilter getFilter(ItemStack stack) {
		return Multitypes.getType(this, stack).getFilter();
	}

}
