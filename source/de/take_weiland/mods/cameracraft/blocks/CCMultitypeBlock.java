package de.take_weiland.mods.cameracraft.blocks;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.take_weiland.mods.commons.templates.Type;
import de.take_weiland.mods.commons.templates.Typed;
import de.take_weiland.mods.commons.util.Blocks;
import de.take_weiland.mods.commons.util.CollectionUtils;

public abstract class CCMultitypeBlock<E extends Type> extends CCBlock implements Typed<E> {

	protected Icon[] icons;
	
	public CCMultitypeBlock(String name, int defaultId, Material material) {
		super(name, defaultId, material);
	}

	@Override
	public E getDefault() {
		return getTypes()[0];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int blockId, CreativeTabs tab, @SuppressWarnings("rawtypes") List list) {
		Blocks.addSubtypes(this, list);
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
