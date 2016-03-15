package de.take_weiland.mods.cameracraft;

import cpw.mods.fml.common.registry.GameRegistry;
import de.take_weiland.mods.cameracraft.blocks.CCBlock;
import de.take_weiland.mods.cameracraft.blocks.MachineType;
import de.take_weiland.mods.cameracraft.blocks.OreType;
import de.take_weiland.mods.cameracraft.item.CCItem;
import de.take_weiland.mods.cameracraft.item.MiscItemType;
import de.take_weiland.mods.cameracraft.item.PhotoType;
import de.take_weiland.mods.commons.util.Blocks;
import de.take_weiland.mods.commons.util.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public final class CCRegistry {

	private CCRegistry() { }
	
	public static void addRecipes() {
		ItemStack tinIngot = CCItem.misc.getStack(MiscItemType.TIN_INGOT);
		ItemStack tinIngot9 = CCItem.misc.getStack(MiscItemType.TIN_INGOT, 9);
		ItemStack blockTin = CCBlock.ores.getStack(OreType.BLOCK_TIN);
		ItemStack oreTin = CCBlock.ores.getStack(OreType.TIN);
		ItemStack photonicDust = CCItem.misc.getStack(MiscItemType.PHOTONIC_DUST);
		ItemStack photoelectricDust = CCItem.misc.getStack(MiscItemType.PHOTOELECTRIC_DUST);
		ItemStack photoPaper = CCItem.misc.getStack(MiscItemType.PHOTO_PAPER);
        ItemStack developer = CCItem.misc.getStack(MiscItemType.DEVELOPER);
        ItemStack fixer = CCItem.misc.getStack(MiscItemType.FIXER);
        ItemStack sulfur = CCItem.misc.getStack(MiscItemType.SULFUR_DUST);

		GameRegistry.addSmelting(oreTin, tinIngot, 0.7f);

		GameRegistry.addRecipe(new ShapedOreRecipe(oreTin,
				"III",
				"III",
				"III",
				'I', "ingotTin"));
		
		GameRegistry.addRecipe(new ShapelessOreRecipe(tinIngot9, blockTin));

        GameRegistry.addShapelessRecipe(photoelectricDust, photonicDust, Items.redstone);

        GameRegistry.addShapedRecipe(photoPaper,
                "DDD",
                "PPP",
                'D', photonicDust,
                'P', Items.paper);

        for (MiscItemType type : MiscItemType.values()) {
            if (type.isInk()) {
                GameRegistry.addShapelessRecipe(CCItem.misc.getStack(type), photonicDust, new ItemStack(Items.dye, 1, type.getDyeMeta()), Items.potionitem);
            }
        }

        GameRegistry.addShapedRecipe(new ItemStack(CCBlock.safetyLight),
                "GGG",
                "GLG",
                "GGG",
                'G', new ItemStack(Blocks.stained_glass, 14),
                'L', Blocks.redstone_lamp);

        GameRegistry.addShapelessRecipe(developer, Items.potionitem, photonicDust);
        GameRegistry.addShapelessRecipe(fixer, Items.potionitem, sulfur);

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
		OreDictionary.registerOre("ingotTin", CCItem.misc.getStack(MiscItemType.TIN_INGOT));
	}

}
