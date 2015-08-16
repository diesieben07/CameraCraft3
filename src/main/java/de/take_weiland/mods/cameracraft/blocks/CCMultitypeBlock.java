package de.take_weiland.mods.cameracraft.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.take_weiland.mods.commons.client.Icons;
import de.take_weiland.mods.commons.meta.HasSubtypes;
import de.take_weiland.mods.commons.meta.Subtype;
import de.take_weiland.mods.commons.meta.Subtypes;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

import java.util.Map;

public abstract class CCMultitypeBlock<TYPE extends Subtype> extends CCBlock implements HasSubtypes<TYPE> {

	protected Map<TYPE, Icon> icons;

	public CCMultitypeBlock(String name, int defaultId, Material material) {
		super(name, material);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int side, int meta) {
		return icons.get(Subtypes.getType(this, meta));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register) {
		icons = Icons.registerMulti(this, register);
	}

	@Override
	public int getDamageValue(World world, int x, int y, int z) {
		return world.getBlockMetadata(x, y, z);
	}
}
