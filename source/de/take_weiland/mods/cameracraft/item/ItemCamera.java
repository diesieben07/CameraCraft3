package de.take_weiland.mods.cameracraft.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import de.take_weiland.mods.commons.templates.Typed;
import de.take_weiland.mods.commons.util.CollectionUtils;
import de.take_weiland.mods.commons.util.Items;

public class ItemCamera extends CCItem implements Typed<CameraType> {

	private Icon[] icons;
	
	public ItemCamera(int defaultId) {
		super("camera", defaultId);
	}

	@Override
	public CameraType[] getTypes() {
		return CameraType.values();
	}

	@Override
	public CameraType getDefault() {
		return CameraType.FILM;
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
		return CollectionUtils.safeArrayAccess(icons, meta);
	}

	@Override
	public void registerIcons(IconRegister register) {
		icons = Items.registerIcons(this, register);
	}

}
