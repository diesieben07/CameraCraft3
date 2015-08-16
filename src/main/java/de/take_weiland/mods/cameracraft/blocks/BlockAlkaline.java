package de.take_weiland.mods.cameracraft.blocks;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;

public class BlockAlkaline extends BlockFluidClassic { // damn you java, we need multiple inheritance :D

	private static class DamageSourceAlkaline extends DamageSource {

		DamageSourceAlkaline() {
			super("cameracraft.alkaline");
			setDamageBypassesArmor();
		}
		
	}
	
	private static final DamageSource ALKALINE_DAMAGE = new DamageSourceAlkaline();
	
	private static final Material ALKALINE_MATERIAL = new MaterialLiquid(MapColor.waterColor) {

		@Override
		public boolean blocksMovement() {
			return true;
		}
		
	};
	private static final String BASE_NAME = "alkaline";
	
	private IIcon iconFlow;
	
	public BlockAlkaline() {
		super(CCBlock.alkalineFluid, ALKALINE_MATERIAL);
	}
	
	void lateInit() {
		CCBlock.lateInitBlock(this, BASE_NAME);
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return meta > 0 ? iconFlow : blockIcon;
	}

	@Override
	public void registerBlockIcons(IIconRegister register) {
		blockIcon = register.registerIcon("cameracraft:alkaline");
		iconFlow = register.registerIcon("cameracraft:alkaline_flowing");
		
		CCBlock.alkalineFluid.setIcons(blockIcon, iconFlow);
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		entity.attackEntityFrom(ALKALINE_DAMAGE, 1);
	}

	@Override
	public boolean canDisplace(IBlockAccess world, int x, int y, int z) {
		return !world.getBlock(x, y, z).getMaterial().isLiquid() && super.canDisplace(world, x, y, z);
	}

	@Override
	public boolean displaceIfPossible(World world, int x, int y, int z) {
		return !world.getBlock(x, y, z).getMaterial().isLiquid() && super.displaceIfPossible(world, x, y, z);
	}

}
