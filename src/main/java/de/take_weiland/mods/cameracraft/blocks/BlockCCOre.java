package de.take_weiland.mods.cameracraft.blocks;

import de.take_weiland.mods.commons.meta.MetadataProperty;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Random;

public class BlockCCOre extends CCSubtypeBlock<OreType> {

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
		ArrayList<ItemStack> list = new ArrayList<>();
        getType(metadata).getDrops(list, world.rand, fortune);
		return list;
	}

    private final Random rand = new Random();

	@Override
	public int getExpDrop(IBlockAccess world, int metadata, int fortune) {
        OreType type = getType(metadata);
        if (type == OreType.PHOTONIC || type == OreType.SULFUR) {
            return MathHelper.getRandomIntegerInRange(rand, 2, 5);
        } else {
            return 0;
        }
	}
}
