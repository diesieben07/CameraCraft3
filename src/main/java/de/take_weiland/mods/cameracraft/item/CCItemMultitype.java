package de.take_weiland.mods.cameracraft.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import de.take_weiland.mods.commons.client.Icons;
import de.take_weiland.mods.commons.templates.HasMetadata;
import de.take_weiland.mods.commons.templates.Metadata.ItemMeta;
import de.take_weiland.mods.commons.util.ItemStacks;
import de.take_weiland.mods.commons.util.JavaUtils;
import de.take_weiland.mods.commons.util.Multitypes;

public abstract class CCItemMultitype<T extends ItemMeta> extends CCItem implements HasMetadata<T> {

	private Icon[] icons;
	private List<ItemStack> subtypes;
	
	public CCItemMultitype(String name, int defaultId) {
		super(name, defaultId);
	}
	
	@Override
	protected void lateInit() {
		super.lateInit();
		subtypes = provideSubtypes();
	}

	protected List<ItemStack> provideSubtypes() {
		return ItemStacks.allOf(this);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public final void getSubItems(int itemId, CreativeTabs tab, List itemList) {
		itemList.addAll(subtypes);
	}

	@Override
	public T getDefault() {
		return getTypes()[0];
	}

	@Override
	public Icon getIconFromDamage(int meta) {
		return JavaUtils.get(icons, meta);
	}

	@Override
	public void registerIcons(IconRegister register) {
		icons = Icons.registerMulti(register, getTypes());
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return Multitypes.name(Multitypes.getType(this, stack));
	}

}
