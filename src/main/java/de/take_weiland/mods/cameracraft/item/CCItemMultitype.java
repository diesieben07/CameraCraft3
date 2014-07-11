package de.take_weiland.mods.cameracraft.item;

import de.take_weiland.mods.commons.client.Icons;
import de.take_weiland.mods.commons.meta.HasSubtypes;
import de.take_weiland.mods.commons.meta.Subtype;
import de.take_weiland.mods.commons.meta.Subtypes;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

import java.util.List;
import java.util.Map;

public abstract class CCItemMultitype<T extends Subtype> extends CCItem implements HasSubtypes<T> {

    private Map<T, Icon> icons;

	public CCItemMultitype(String name, int defaultId) {
		super(name, defaultId);
	}
	
	@Override
	protected void lateInit() {
		super.lateInit();
	}

	@Override
	public final void getSubItems(int itemId, CreativeTabs tab, List list) {
        Subtypes.getSubItemsImpl(this, list);
    }

	@Override
	public Icon getIconFromDamage(int meta) {
		return icons.get(subtypeProperty().value(meta));
	}

	@Override
	public void registerIcons(IconRegister register) {
		icons = Icons.registerMulti(this, register);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return Subtypes.name(this, stack);
	}

}
