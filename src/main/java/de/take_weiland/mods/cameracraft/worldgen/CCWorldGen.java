package de.take_weiland.mods.cameracraft.worldgen;

import cpw.mods.fml.common.IWorldGenerator;
import de.take_weiland.mods.cameracraft.blocks.CCBlock;
import de.take_weiland.mods.cameracraft.blocks.OreType;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.terraingen.TerrainGen;

import java.util.Random;

public class CCWorldGen implements IWorldGenerator {

	public static final OreGenEvent.GenerateMinable.EventType TIN = EnumHelper.addEnum(OreGenEvent.GenerateMinable.EventType.class, "CAMERACRAFT_TIN", new Class[0], new Object[0]);
	public static final OreGenEvent.GenerateMinable.EventType ALKALINE = EnumHelper.addEnum(OreGenEvent.GenerateMinable.EventType.class, "CAMERACRAFT_ALKALINE", new Class[0], new Object[0]);
	public static final OreGenEvent.GenerateMinable.EventType PHOTONIC = EnumHelper.addEnum(OreGenEvent.GenerateMinable.EventType.class, "CAMERACRAFT_PHOTONIC", new Class[0], new Object[0]);

	private enum Ore {

		TIN(CCWorldGen.TIN, OreType.TIN, 20, 0, 64, 8),
		ALKALINE(CCWorldGen.ALKALINE, OreType.ALKALINE, 10, 0, 30, 8),
		PHOTONIC(CCWorldGen.PHOTONIC, OreType.PHOTONIC, 10, 0, 128, 5, Blocks.netherrack);

		final OreGenEvent.GenerateMinable.EventType type;
		final WorldGenerator gen;
		final int amount;
		final int minY;
		final int maxY;

		Ore(OreGenEvent.GenerateMinable.EventType eventType, OreType oreType, int amount, int minY, int maxY, int diameter) {
			this(eventType, oreType, amount, minY, maxY, diameter, Blocks.stone);
		}

		Ore(OreGenEvent.GenerateMinable.EventType eventType, OreType oreType, int amount, int minY, int maxY, int diameter, Block replace) {
			this.type = eventType;
			this.amount = amount;
			this.minY = minY;
			this.maxY = maxY;
			this.gen = new WorldGenMinable(CCBlock.ores, oreType.ordinal(), diameter, replace);
		}

	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		switch (world.provider.dimensionId) {
		case 0:
			generate(Ore.TIN, world, chunkX, chunkZ, random);
			generate(Ore.ALKALINE, world, chunkX, chunkZ, random);
			break;
		case -1:
			generate(Ore.PHOTONIC, world, chunkX, chunkZ, random);
			break;
		}
	}

	private void generate(Ore ore, World world, int chunkX, int chunkZ, Random rand) {
		if (TerrainGen.generateOre(world, rand, ore.gen, chunkX, chunkZ, ore.type)) {
			for (int i = 0; i < ore.amount; ++i) {
				int x = chunkX * 16 + rand.nextInt(16);
				int y = rand.nextInt(ore.maxY - ore.minY) + ore.minY;
				int z = chunkZ * 16 + rand.nextInt(16);
				ore.gen.generate(world, rand, x, y, z);
			}
		}
	}

}
