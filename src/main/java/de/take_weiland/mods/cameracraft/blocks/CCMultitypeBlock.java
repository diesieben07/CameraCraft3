package de.take_weiland.mods.cameracraft.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.take_weiland.mods.commons.client.Icons;
import de.take_weiland.mods.commons.meta.HasSubtypes;
import de.take_weiland.mods.commons.meta.Subtype;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.Map;

public abstract class CCMultitypeBlock<TYPE extends Subtype> extends CCBlock implements HasSubtypes<TYPE> {

	protected Map<TYPE, IIcon> icons;

	public CCMultitypeBlock(String name, Material material) {
		super(name, material);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return icons.get(getType(meta));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister register) {
		icons = Icons.registerMulti(this, register);
	}

	@Override
	public int getDamageValue(World world, int x, int y, int z) {
		return world.getBlockMetadata(x, y, z);
	}
}
