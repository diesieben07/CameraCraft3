package de.take_weiland.mods.cameracraft.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.commons.util.Blocks;
import de.take_weiland.mods.commons.util.Inventories;

public class CCBlock extends Block {

	public static BlockCCOre ores;
	public static BlockCCMachine machines;
	public static BlockCable cable;
	public static BlockAlkaline alkaline;
	
	public static final void createBlocks() {
		alkalineFluid = new Fluid("cameracraft.alkaline").setLuminosity(7);
		FluidRegistry.registerFluid(alkalineFluid);
		
		(ores = new BlockCCOre(3078)).lateInit();
		(machines = new BlockCCMachine(3079)).lateInit();
		(cable = new BlockCable(3080)).lateInit();
		(alkaline = new BlockAlkaline(3081)).lateInit();
	}
	
	static final int getId(String name, int defaultId) {
		return CameraCraft.config.getBlock(name, defaultId).getInt();
	}
	
	private final String baseName;
	public static Fluid alkalineFluid;
	
	protected CCBlock(String name, int defaultId, Material material) {
		super(getId(name, defaultId), material);
		this.baseName = name;
	}
	
	protected void lateInit() {
		lateInitBlock(this, baseName);
	}

	static void lateInitBlock(Block block, String baseName) {
		Blocks.init(block, baseName);
		block.setCreativeTab(CameraCraft.tab);
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, int blockId, int metadata) {
		if (hasTileEntity(metadata)) {
			Inventories.spillIfInventory(world.getBlockTileEntity(x, y, z));
		}
		super.breakBlock(world, x, y, z, blockId, metadata);
	}

}
