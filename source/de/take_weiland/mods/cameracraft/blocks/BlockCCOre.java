package de.take_weiland.mods.cameracraft.blocks;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.take_weiland.mods.commons.templates.Typed;
import de.take_weiland.mods.commons.util.Blocks;
import de.take_weiland.mods.commons.util.CommonUtils;
import de.take_weiland.mods.commons.util.Items;

public class BlockCCOre extends CCBlock implements Typed<OreType> {

	private Icon[] icons;
	
	public BlockCCOre(int defaultId) {
		super("ores", defaultId, Material.rock);
	}

	@Override
	public OreType[] getTypes() {
		return OreType.values();
	}

	@Override
	public OreType getDefault() {
		return OreType.TIN;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int blockId, CreativeTabs tab, @SuppressWarnings("rawtypes") List list) {
		Blocks.addSubtypes(this, list);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return getUnlocalizedName() + "." + Items.getType(this, stack).getName();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int side, int meta) {
		return CommonUtils.safeArrayAccess(icons, meta);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register) {
		icons = Blocks.registerIcons(this, register);
	}

}
