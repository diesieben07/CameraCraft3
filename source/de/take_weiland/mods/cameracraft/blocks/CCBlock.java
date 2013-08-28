package de.take_weiland.mods.cameracraft.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.commons.util.Blocks;
import de.take_weiland.mods.commons.util.Inventories;

public class CCBlock extends Block {

	public static BlockCCOre ores;
	public static BlockCCMachine machines;

	public static final void createBlocks() {
		ores = new BlockCCOre(3078);
		machines = new BlockCCMachine(3079);
		
		MachineType.registerTileEntities();
		
		OreDictionary.registerOre("oreTin", Blocks.getStack(ores, OreType.TIN));
		OreDictionary.registerOre("oreAlkaline", Blocks.getStack(ores, OreType.ALKALINE));
	}
	
	private static final int getId(String name, int defaultId) {
		return CameraCraft.config.getBlock(name, defaultId).getInt();
	}
	
	protected CCBlock(String name, int defaultId, Material material) {
		super(getId(name, defaultId), material);
		
		Blocks.init(this, name);
		setCreativeTab(CameraCraft.tab);
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, int blockId, int metadata) {
		if (hasTileEntity(metadata)) {
			Inventories.spillIfInventory(world.getBlockTileEntity(x, y, z));
		}
		super.breakBlock(world, x, y, z, blockId, metadata);
	}

}
