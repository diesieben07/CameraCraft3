package de.take_weiland.mods.cameracraft;

import static de.take_weiland.mods.cameracraft.blocks.CCBlock.ores;
import static de.take_weiland.mods.commons.util.Constants.CLOTH_BLACK;
import static de.take_weiland.mods.commons.util.Constants.CLOTH_GRAY;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cpw.mods.fml.common.registry.GameRegistry;
import de.take_weiland.mods.cameracraft.blocks.CableType;
import de.take_weiland.mods.cameracraft.blocks.OreType;
import de.take_weiland.mods.cameracraft.item.MiscItemType;
import de.take_weiland.mods.commons.util.Constants;
import de.take_weiland.mods.commons.util.ItemStacks;

public final class CCRecipes {

	private CCRecipes() { }
	
	@SuppressWarnings("boxing")
	static void addRecipes() {
		FurnaceRecipes f = FurnaceRecipes.smelting();
		
		ItemStack tinIngot = ItemStacks.of(MiscItemType.TIN_INGOT);
		ItemStack tinIngot9 = ItemStacks.of(MiscItemType.TIN_INGOT, 9);
		ItemStack blockTin = ItemStacks.of(OreType.BLOCK_TIN);
		ItemStack cablePower8 = ItemStacks.of(CableType.POWER);
		ItemStack cableData8 = ItemStacks.of(CableType.DATA);
		ItemStack photonicDust = ItemStacks.of(MiscItemType.PHOTONIC_DUST);
		
		f.addSmelting(ores.blockID, OreType.TIN.ordinal(), tinIngot, 0.7f);
		
		GameRegistry.addRecipe(new ShapedOreRecipe(blockTin, 
				"III",
				"III",
				"III",
				'I', "ingotTin"));
		
		GameRegistry.addRecipe(new ShapelessOreRecipe(tinIngot9, blockTin));
		
		GameRegistry.addRecipe(cablePower8, 
				"WWW",
				"WRW",
				"WWW",
				'W', new ItemStack(Block.cloth, 1, CLOTH_BLACK),
				'R', Block.blockRedstone
			);
		
		GameRegistry.addRecipe(cableData8, 
				"WWW",
				"WLW",
				"WWW",
				'W', new ItemStack(Block.cloth, 1, CLOTH_GRAY),
				'L', Block.blockLapis
			);
		
		GameRegistry.addShapelessRecipe(ItemStacks.of(MiscItemType.BLACK_INK), photonicDust, new ItemStack(Item.dyePowder, 1, Constants.DYE_BLACK));
		GameRegistry.addShapelessRecipe(ItemStacks.of(MiscItemType.YELLOW_INK), photonicDust, new ItemStack(Item.dyePowder, 1, Constants.DYE_YELLOW));
		GameRegistry.addShapelessRecipe(ItemStacks.of(MiscItemType.MAGENTA_INK), photonicDust, new ItemStack(Item.dyePowder, 1, Constants.DYE_MAGENTA));
		GameRegistry.addShapelessRecipe(ItemStacks.of(MiscItemType.CYAN_INK), photonicDust, new ItemStack(Item.dyePowder, 1, Constants.DYE_CYAN));
	}

}
