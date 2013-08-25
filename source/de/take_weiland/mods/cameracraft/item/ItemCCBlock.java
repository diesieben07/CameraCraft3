package de.take_weiland.mods.cameracraft.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import de.take_weiland.mods.cameracraft.blocks.CCBlock;

public class ItemCCBlock extends ItemBlock {

	private final CCBlock block;
	
	public ItemCCBlock(int itemId, Block block) {
		super(itemId);
		this.block = (CCBlock)block;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return block.getUnlocalizedName(stack);
	}

}
