package de.take_weiland.mods.cameracraft.blocks;

import com.google.common.collect.Lists;
import de.take_weiland.mods.cameracraft.item.CCItem;
import de.take_weiland.mods.commons.meta.MetadataProperty;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.ArrayList;

public class BlockCCOre extends CCMultitypeBlock<OreType> {

	private static final MetadataProperty<OreType> subtypeProp = MetadataProperty.newProperty(0, OreType.class);

	public BlockCCOre() {
		super("ores", Material.rock);
		setHardness(3);
		setResistance(5);
		setStepSound(soundTypeStone);
        setHarvestLevel("pickaxe", ItemTool.ToolMaterial.IRON.getHarvestLevel());
	}

    @Override
	public MetadataProperty<OreType> subtypeProperty() {
		return subtypeProp;
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
		ArrayList<ItemStack> result;
		if (getType(metadata) == OreType.ALKALINE) {
			int amount = 2 + world.rand.nextInt(2 + fortune);
			result = Lists.newArrayListWithCapacity(amount);

			int dropMeta = subtypeProp.toMeta(OreType.ALKALINE, 0);

			for (int i = 0; i < amount; ++i) {
				result.add(new ItemStack(CCItem.misc, 1, dropMeta));
			}
		} else {
			result = Lists.newArrayListWithCapacity(1);
			result.add(new ItemStack(this));
		}
		return result;
	}

	@Override
	public void dropBlockAsItemWithChance(World world, int x, int y, int z, int meta, float destroyChance, int fortune) {
		super.dropBlockAsItemWithChance(world, x, y, z, meta, destroyChance, fortune);
		if (getType(meta) == OreType.ALKALINE) {
			dropXpOnBlockBreak(world, x, y, z, MathHelper.getRandomIntegerInRange(world.rand, 2, 5));
		}
	}

}
