package de.take_weiland.mods.cameracraft;

import static de.take_weiland.mods.cameracraft.blocks.CCBlock.ores;
import static de.take_weiland.mods.cameracraft.item.CCItem.ingotsDusts;
import static de.take_weiland.mods.commons.util.Blocks.getStack;
import static de.take_weiland.mods.commons.util.Items.getStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cpw.mods.fml.common.registry.GameRegistry;
import de.take_weiland.mods.cameracraft.blocks.OreType;
import de.take_weiland.mods.cameracraft.item.IngotDustType;

public final class CCRecipes {

	private CCRecipes() { }
	
	@SuppressWarnings("boxing")
	static void addRecipes() {
		FurnaceRecipes f = FurnaceRecipes.smelting();
		f.addSmelting(ores.blockID, OreType.TIN.getMeta(), getStack(ingotsDusts, IngotDustType.TIN_INGOT), 0.7f);
		
		ItemStack blockTin = getStack(ores, OreType.BLOCK_TIN);
		
		GameRegistry.addRecipe(new ShapedOreRecipe(blockTin, 
				"III",
				"III",
				"III",
				'I', "ingotTin"));
		
		GameRegistry.addRecipe(new ShapelessOreRecipe(getStack(ingotsDusts, IngotDustType.TIN_INGOT, 9), blockTin));
	}

}
