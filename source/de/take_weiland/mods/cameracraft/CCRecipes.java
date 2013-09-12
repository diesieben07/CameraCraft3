package de.take_weiland.mods.cameracraft;

import static de.take_weiland.mods.cameracraft.blocks.CCBlock.ores;
import static de.take_weiland.mods.cameracraft.blocks.CableTypeInt.DATA;
import static de.take_weiland.mods.cameracraft.blocks.CableTypeInt.POWER;
import static de.take_weiland.mods.cameracraft.item.IngotDustType.TIN_INGOT;
import static de.take_weiland.mods.commons.util.Constants.CLOTH_BLACK;
import static de.take_weiland.mods.commons.util.Constants.CLOTH_GRAY;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cpw.mods.fml.common.registry.GameRegistry;
import de.take_weiland.mods.cameracraft.blocks.OreType;

public final class CCRecipes {

	private CCRecipes() { }
	
	@SuppressWarnings("boxing")
	static void addRecipes() {
		FurnaceRecipes f = FurnaceRecipes.smelting();
		f.addSmelting(ores.blockID, OreType.TIN.ordinal(), TIN_INGOT.stack(), 0.7f);
		
		ItemStack blockTin = OreType.BLOCK_TIN.stack();
		
		GameRegistry.addRecipe(new ShapedOreRecipe(blockTin, 
				"III",
				"III",
				"III",
				'I', "ingotTin"));
		
		GameRegistry.addRecipe(new ShapelessOreRecipe(TIN_INGOT.stack(9), blockTin));
		
		GameRegistry.addRecipe(POWER.stack(8), 
				"WWW",
				"WRW",
				"WWW",
				'W', new ItemStack(Block.cloth, 1, CLOTH_BLACK),
				'R', Block.blockRedstone
			);
		
		GameRegistry.addRecipe(DATA.stack(8), 
				"WWW",
				"WLW",
				"WWW",
				'W', new ItemStack(Block.cloth, 1, CLOTH_GRAY),
				'L', Block.blockLapis
			);
	}

}
