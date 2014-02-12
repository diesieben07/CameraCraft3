package de.take_weiland.mods.cameracraft.blocks;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.take_weiland.mods.commons.client.Icons;
import de.take_weiland.mods.commons.templates.HasMetadata;
import de.take_weiland.mods.commons.templates.Metadata.BlockMeta;
import de.take_weiland.mods.commons.util.ItemStacks;
import de.take_weiland.mods.commons.util.JavaUtils;
import de.take_weiland.mods.commons.util.Multitypes;

public abstract class CCMultitypeBlock<TYPE extends BlockMeta> extends CCBlock implements HasMetadata<TYPE> {

	protected Icon[] icons;
	private List<ItemStack> subtypes;
	
	public CCMultitypeBlock(String name, int defaultId, Material material) {
		super(name, defaultId, material);
	}
	
	@Override
	protected void lateInit() {
		super.lateInit();
		subtypes = provideSubtypes();
	}

	protected List<ItemStack> provideSubtypes() {
		return ItemStacks.allOf(this);
	}

	@Override
	public TYPE getDefault() {
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
		return JavaUtils.get(icons, meta);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register) {
		icons = Icons.registerMulti(register, getTypes());
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
		return ItemStacks.of(Multitypes.getType(this, world.getBlockMetadata(x, y, z)));
	}

}
