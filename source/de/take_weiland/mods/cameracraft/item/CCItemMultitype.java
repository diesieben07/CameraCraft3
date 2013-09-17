package de.take_weiland.mods.cameracraft.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import de.take_weiland.mods.commons.templates.Named;
import de.take_weiland.mods.commons.templates.Type;
import de.take_weiland.mods.commons.templates.Typed;
import de.take_weiland.mods.commons.util.CollectionUtils;
import de.take_weiland.mods.commons.util.Items;
import de.take_weiland.mods.commons.util.Multitypes;

public abstract class CCItemMultitype<T extends Type<T>> extends CCItem implements Typed<T> {

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
		return Multitypes.allStacks(this);
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
		return CollectionUtils.safeArrayAccess(icons, meta);
	}

	@Override
	public void registerIcons(IconRegister register) {
		icons = Items.registerIcons(this, register);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return subtypeNameInternal(Multitypes.getType(stack), "");
	}
	
	private String subtypeNameInternal(Named named, String suffix) {
		return getUnlocalizedName() + "." + named.unlocalizedName() + suffix;
	}

	@Override
	public String subtypeName(T subtype) {
		return subtypeNameInternal(subtype, ".name");
	}

}
