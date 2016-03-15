package de.take_weiland.mods.cameracraft.worldgen;

import cpw.mods.fml.common.IWorldGenerator;
import de.take_weiland.mods.cameracraft.api.OreGenerator;
import de.take_weiland.mods.cameracraft.blocks.CCBlock;
import de.take_weiland.mods.cameracraft.blocks.OreType;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.terraingen.TerrainGen;

import java.util.Random;

public class CCWorldGen implements IWorldGenerator {

	private enum OreInternal {

		TIN(OreGenerator.Ore.TIN, OreType.TIN, 20, 0, 64, 8),
		PHOTONIC(OreGenerator.Ore.PHOTONIC, OreType.PHOTONIC, 10, 0, 128, 5, Blocks.netherrack);

		final OreGenerator.Ore type;
		final CCOreGen gen;
		final int amount;
		final int minY;
		final int maxY;

		OreInternal(OreGenerator.Ore type, OreType oreType, int amount, int minY, int maxY, int diameter) {
			this(type, oreType, amount, minY, maxY, diameter, Blocks.stone);
		}

		OreInternal(OreGenerator.Ore type, OreType oreType, int amount, int minY, int maxY, int diameter, Block replace) {
			this.type = type;
			this.amount = amount;
			this.minY = minY;
			this.maxY = maxY;
			this.gen = new CCOreGen(type, CCBlock.ores, oreType.ordinal(), diameter, replace);
		}

	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		switch (world.provider.dimensionId) {
		case 0:
			generate(OreInternal.TIN, world, chunkX, chunkZ, random);
			break;
		case -1:
			generate(OreInternal.PHOTONIC, world, chunkX, chunkZ, random);
			break;
		}
	}

	private void generate(OreInternal ore, World world, int chunkX, int chunkZ, Random rand) {
		if (TerrainGen.generateOre(world, rand, ore.gen, chunkX, chunkZ, OreGenEvent.GenerateMinable.EventType.CUSTOM)) {
			for (int i = 0; i < ore.amount; ++i) {
				int x = chunkX * 16 + rand.nextInt(16);
				int y = rand.nextInt(ore.maxY - ore.minY) + ore.minY;
				int z = chunkZ * 16 + rand.nextInt(16);
				ore.gen.generate(world, rand, x, y, z);
			}
		}
	}

	private static final class CCOreGen extends WorldGenMinable implements OreGenerator {

        private final Ore ore;

        public CCOreGen(Ore ore, Block block, int meta, int number, Block target) {
            super(block, meta, number, target);
            this.ore = ore;
        }

        @Override
        public Ore getOre() {
            return ore;
        }
    }

}
