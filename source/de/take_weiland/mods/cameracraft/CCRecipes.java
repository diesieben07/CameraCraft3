package de.take_weiland.mods.cameracraft;

import static de.take_weiland.mods.cameracraft.blocks.CCBlock.ores;
import static de.take_weiland.mods.cameracraft.item.CCItem.ingotsDusts;
import static de.take_weiland.mods.commons.util.Items.getStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import de.take_weiland.mods.cameracraft.blocks.OreType;
import de.take_weiland.mods.cameracraft.item.IngotDustType;

public final class CCRecipes {

	private CCRecipes() { }
	
	static void addRecipes() {
		FurnaceRecipes f = FurnaceRecipes.smelting();
		
		f.addSmelting(ores.blockID, OreType.TIN.getMeta(), getStack(ingotsDusts, IngotDustType.TIN_INGOT), 0.7f);
	}

}
