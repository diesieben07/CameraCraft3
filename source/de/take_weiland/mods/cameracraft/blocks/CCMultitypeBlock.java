package de.take_weiland.mods.cameracraft.blocks;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.take_weiland.mods.commons.templates.Type;
import de.take_weiland.mods.commons.templates.Typed;
import de.take_weiland.mods.commons.util.Blocks;
import de.take_weiland.mods.commons.util.CollectionUtils;
import de.take_weiland.mods.commons.util.Multitypes;

public abstract class CCMultitypeBlock<E extends Type> extends CCBlock implements Typed<E> {

	protected Icon[] icons;
	private final List<ItemStack> subtypes;
	
	public CCMultitypeBlock(String name, int defaultId, Material material) {
		super(name, defaultId, material);
		subtypes = provideSubtypes();
	}
	
	protected List<ItemStack> provideSubtypes() {
		return Multitypes.allStacks(this);
	}

	@Override
	public E getDefault() {
		return getTypes()[0];
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int blockId, CreativeTabs tab, List list) {
		list.addAll(subtypes);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int side, int meta) {
		return CollectionUtils.safeArrayAccess(icons, meta);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register) {
		icons = Blocks.registerIcons(this, register);
	}
}
