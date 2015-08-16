package de.take_weiland.mods.cameracraft.item;

import de.take_weiland.mods.commons.client.Icons;
import de.take_weiland.mods.commons.meta.HasSubtypes;
import de.take_weiland.mods.commons.meta.Subtype;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;
import java.util.Map;

public abstract class CCItemMultitype<T extends Subtype> extends CCItem implements HasSubtypes<T> {

    private Map<T, IIcon> icons;

	public CCItemMultitype(String name) {
		super(name);
	}
	
	@Override
	public final void getSubItems(Item item, CreativeTabs tab, List list) {
		HasSubtypes.getSubItemsImpl(this, list);
    }

	@Override
	public IIcon getIconFromDamage(int meta) {
		return icons.get(subtypeProperty().value(meta));
	}

	@Override
	public void registerIcons(IIconRegister register) {
		icons = Icons.registerMulti(this, register);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return HasSubtypes.name(this, stack);
	}

}
