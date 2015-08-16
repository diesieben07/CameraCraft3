package de.take_weiland.mods.cameracraft.item;

import de.take_weiland.mods.cameracraft.api.camera.LensItem;
import de.take_weiland.mods.cameracraft.api.img.ImageFilter;
import de.take_weiland.mods.commons.meta.MetadataProperty;
import net.minecraft.item.ItemStack;

public class ItemLens extends CCItemMultitype<LensType> implements LensItem {

    private static final MetadataProperty<LensType> subtypeProp = MetadataProperty.newProperty(0, LensType.class);

	public ItemLens() {
		super("lens");
	}

    @Override
    public MetadataProperty<LensType> subtypeProperty() {
        return subtypeProp;
    }

	@Override
	public ImageFilter getFilter(ItemStack stack) {
		return getType(stack).getFilter();
	}

}
