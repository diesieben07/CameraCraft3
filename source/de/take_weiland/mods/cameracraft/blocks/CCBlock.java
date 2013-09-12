package de.take_weiland.mods.cameracraft.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.registry.GameRegistry;
import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.cameracraft.tileentity.TileEntityCable;
import de.take_weiland.mods.commons.templates.AdvancedBlock;
import de.take_weiland.mods.commons.util.Blocks;
import de.take_weiland.mods.commons.util.Inventories;

public class CCBlock extends Block implements AdvancedBlock {

	public static BlockCCOre ores;
	public static BlockCCMachine machines;
	public static BlockCable cable;

	public static final void createBlocks() {
		ores = new BlockCCOre(3078);
		machines = new BlockCCMachine(3079);
		cable = new BlockCable(3080);
		
		MachineType.registerTileEntities();
		GameRegistry.registerTileEntity(TileEntityCable.class, "cameracraft.cable");
		
		OreDictionary.registerOre("oreTin", OreType.TIN.stack());
		OreDictionary.registerOre("oreAlkaline", OreType.ALKALINE.stack());
		OreDictionary.registerOre("oreGold", Block.stone);
		OreDictionary.registerOre("oreGold", Block.stoneBrick);
		OreDictionary.registerOre("oreGold", Block.stoneButton);
		OreDictionary.registerOre("oreGold", Block.whiteStone);
		OreDictionary.registerOre("oreGold", Block.waterlily);
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

	@Override
	public ItemStack stack() {
		return stack(1, 0);
	}

	@Override
	public ItemStack stack(int quantity) {
		return stack(quantity, 0);
	}

	@Override
	public ItemStack stack(int quantity, int meta) {
		return new ItemStack(this, quantity, meta);
	}

}
