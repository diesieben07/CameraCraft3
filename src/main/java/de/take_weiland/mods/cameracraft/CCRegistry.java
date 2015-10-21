package de.take_weiland.mods.cameracraft;

import cpw.mods.fml.common.registry.GameRegistry;
import de.take_weiland.mods.cameracraft.blocks.CCBlock;
import de.take_weiland.mods.cameracraft.blocks.MachineType;
import de.take_weiland.mods.cameracraft.blocks.OreType;
import de.take_weiland.mods.cameracraft.item.CCItem;
import de.take_weiland.mods.cameracraft.item.MiscItemType;
import de.take_weiland.mods.cameracraft.item.PhotoType;
import de.take_weiland.mods.commons.util.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import static net.minecraftforge.fluids.FluidContainerRegistry.BUCKET_VOLUME;

public final class CCRegistry {

	private CCRegistry() { }
	
	@SuppressWarnings("boxing")
	public static void addRecipes() {
		FurnaceRecipes f = FurnaceRecipes.smelting();
		
		ItemStack tinIngot = CCItem.misc.getStack(MiscItemType.TIN_INGOT);
		ItemStack tinIngot9 = CCItem.misc.getStack(MiscItemType.TIN_INGOT, 9);
		ItemStack blockTin = CCBlock.ores.getStack(OreType.BLOCK_TIN);
		ItemStack oreTin = CCBlock.ores.getStack(OreType.TIN);
		ItemStack photonicDust = CCItem.misc.getStack(MiscItemType.PHOTONIC_DUST);

		GameRegistry.addSmelting(oreTin, tinIngot, 0.7f);

		GameRegistry.addRecipe(new ShapedOreRecipe(oreTin,
				"III",
				"III",
				"III",
				'I', "ingotTin"));
		
		GameRegistry.addRecipe(new ShapelessOreRecipe(tinIngot9, blockTin));
		
		GameRegistry.addShapelessRecipe(CCItem.misc.getStack(MiscItemType.BLACK_INK), photonicDust, new ItemStack(Items.dye, 1, 0));
		GameRegistry.addShapelessRecipe(CCItem.misc.getStack(MiscItemType.YELLOW_INK), photonicDust, new ItemStack(Items.dye, 1, 11));
		GameRegistry.addShapelessRecipe(CCItem.misc.getStack(MiscItemType.MAGENTA_INK), photonicDust, new ItemStack(Items.dye, 1, 13));
		GameRegistry.addShapelessRecipe(CCItem.misc.getStack(MiscItemType.CYAN_INK), photonicDust, new ItemStack(Items.dye, 1, 6));

		((ShapedRecipes) GameRegistry.addShapedRecipe(CCItem.photo.getStack(PhotoType.POSTER),
				"SSS",
				"SPS",
				"SSS",
				'S', Items.stick,
				'P', CCItem.photo.getStack(PhotoType.PHOTO))).func_92100_c();
	}
	
	public static void doMiscRegistering() {
		MachineType.registerTileEntities();

		OreDictionary.registerOre("oreTin", CCBlock.ores.getStack(OreType.TIN));
		OreDictionary.registerOre("oreAlkaline", CCBlock.ores.getStack(OreType.ALKALINE));

		OreDictionary.registerOre("ingotTin", CCItem.misc.getStack(MiscItemType.TIN_INGOT));

		FluidContainerRegistry.registerFluidContainer(new FluidStack(CCBlock.alkalineFluid, BUCKET_VOLUME), CCItem.misc.getStack(MiscItemType.ALKALINE_BUCKET), new ItemStack(Items.bucket));
	}

}
