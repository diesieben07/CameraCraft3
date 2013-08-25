package de.take_weiland.mods.cameracraft.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import de.take_weiland.mods.commons.templates.Typed;
import de.take_weiland.mods.commons.util.CommonUtils;
import de.take_weiland.mods.commons.util.Items;

public class ItemIngotsDusts extends CCItem implements Typed<IngotDustType> {

	private Icon[] icons;
	
	public ItemIngotsDusts(int defaultId) {
		super("ingotsDusts", defaultId);
	}

	@Override
	public IngotDustType[] getTypes() {
		return IngotDustType.values();
	}

	@Override
	public IngotDustType getDefault() {
		return IngotDustType.TIN_INGOT;
	}
	
	@Override
	public void getSubItems(int itemId, CreativeTabs tab, @SuppressWarnings("rawtypes") List itemList) {
		Items.addSubtypes(this, itemList);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return Items.getUnlocalizedName(this, stack);
	}

	@Override
	public Icon getIconFromDamage(int meta) {
		return CommonUtils.safeArrayAccess(icons, meta);
	}

	@Override
	public void registerIcons(IconRegister register) {
		icons = Items.registerIcons(this, register);
	}

}
