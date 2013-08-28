package de.take_weiland.mods.cameracraft.blocks;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import de.take_weiland.mods.cameracraft.item.CCItem;
import de.take_weiland.mods.commons.util.Multitypes;

public class BlockCCOre extends CCMultitypeBlock<OreType> {

	public BlockCCOre(int defaultId) {
		super("ores", defaultId, Material.rock);
		setHardness(3);
		setResistance(5);
		setStepSound(soundStoneFootstep);
		MinecraftForge.setBlockHarvestLevel(this, "pickaxe", EnumToolMaterial.IRON.getHarvestLevel());
	}

	@Override
	public int idDropped(int meta, Random random, int fortune) {
		if (Multitypes.getType(this, meta) == OreType.ALKALINE) {
			return CCItem.battery.itemID;
		} else {
			return blockID;
		}
	}

	@Override
	public int quantityDropped(int meta, int fortune, Random random) {
		if (Multitypes.getType(this, meta) == OreType.ALKALINE) {
			return 2 + random.nextInt(2 + fortune);
		} else {
			return 1;
		}
	}

	@Override
	public void dropBlockAsItemWithChance(World world, int x, int y, int z, int meta, float destroyChance, int fortune) {
		super.dropBlockAsItemWithChance(world, x, y, z, meta, destroyChance, fortune);
		if (Multitypes.getType(this, meta) == OreType.ALKALINE) {
			dropXpOnBlockBreak(world, x, y, z, MathHelper.getRandomIntegerInRange(world.rand, 2, 5));
		}
	}

	@Override
	public OreType[] getTypes() {
		return OreType.values();
	}
	
}
