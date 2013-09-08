package de.take_weiland.mods.cameracraft;

import static de.take_weiland.mods.cameracraft.blocks.CCBlock.*;
import static de.take_weiland.mods.cameracraft.item.CCItem.*;
import static de.take_weiland.mods.commons.util.Blocks.getStack;
import static de.take_weiland.mods.commons.util.Items.getStack;
import static de.take_weiland.mods.cameracraft.item.IngotDustType.*;
import static de.take_weiland.mods.cameracraft.blocks.CableTypeInt.*;
import static de.take_weiland.mods.commons.util.Constants.*;
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
		f.addSmelting(ores.blockID, OreType.TIN.getMeta(), getStack(ingotsDusts, TIN_INGOT), 0.7f);
		
		ItemStack blockTin = getStack(ores, OreType.BLOCK_TIN);
		
		GameRegistry.addRecipe(new ShapedOreRecipe(blockTin, 
				"III",
				"III",
				"III",
				'I', "ingotTin"));
		
		GameRegistry.addRecipe(new ShapelessOreRecipe(getStack(ingotsDusts, TIN_INGOT, 9), blockTin));
		
		GameRegistry.addRecipe(getStack(cable, POWER, 8), 
				"WWW",
				"WRW",
				"WWW",
				'W', new ItemStack(Block.cloth, 1, CLOTH_BLACK),
				'R', Block.blockRedstone
			);
		
		GameRegistry.addRecipe(getStack(cable, DATA, 8), 
				"WWW",
				"WLW",
				"WWW",
				'W', new ItemStack(Block.cloth, 1, CLOTH_GRAY),
				'L', Block.blockLapis
			);
	}

}
