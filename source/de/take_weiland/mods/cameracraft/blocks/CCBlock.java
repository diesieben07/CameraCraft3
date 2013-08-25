package de.take_weiland.mods.cameracraft.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.cameracraft.item.ItemCCBlock;
import de.take_weiland.mods.commons.util.Blocks;

public class CCBlock extends Block {

	public static BlockCCOre ores;
	public static BlockCCMachine machines;

	public static final void createBlocks() {
		ores = new BlockCCOre(3078);
		machines = new BlockCCMachine(3079);
	}
	
	public String getUnlocalizedName(ItemStack stack) {
		return getUnlocalizedName(stack.getItemDamage());
	}
	
	public String getUnlocalizedName(int meta) {
		return getUnlocalizedName();
	}

	protected CCBlock(String name, int defaultId, Material material) {
		super(getId(name, defaultId), material);
		
		Blocks.init(this, name, ItemCCBlock.class);
		setCreativeTab(CameraCraft.tab);
	}

	private static final int getId(String name, int defaultId) {
		return CameraCraft.config.getBlock(name, defaultId).getInt();
	}
	
}
