package de.take_weiland.mods.cameracraft.worldgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.EnumHelper;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType;
import net.minecraftforge.event.terraingen.TerrainGen;
import de.take_weiland.mods.cameracraft.blocks.CCBlock;
import de.take_weiland.mods.cameracraft.blocks.OreType;

public class CCWorldGen {

	public static final EventType TIN_EVENT_TYPE = EnumHelper.addEnum(EventType.class, "CAMERACRAFT_TIN", new Class[0], new Object[0]);
	
	private final WorldGenerator tinGenerator = new WorldGenMinable(CCBlock.ores.blockID, OreType.TIN.getMeta(), 5, Block.stone.blockID);
	
	@ForgeSubscribe
	public void generate(OreGenEvent.Post event) {
		generate(event.rand, event.worldX, event.worldZ, event.world);
	}
	
	private void generate(Random random, int chunkX, int chunkZ, World world) {
		generateOre(20, 0, 64, world, chunkX, chunkZ, random, tinGenerator);
	}
	
	private void generateOre(int amount, int minY, int maxY, World world, int chunkX, int chunkZ, Random random, WorldGenerator generator) {
		if (TerrainGen.generateOre(world, random, generator, chunkX, chunkZ, TIN_EVENT_TYPE)) {
			for (int i = 0; i < amount; i++) {
				int x = chunkX + random.nextInt(16);
				int y = random.nextInt(maxY - minY) + minY;
				int z = chunkZ + random.nextInt(16);
				generator.generate(world, random, x, y, z);
			}
		}
	}

}
