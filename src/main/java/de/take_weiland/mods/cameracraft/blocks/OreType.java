package de.take_weiland.mods.cameracraft.blocks;

import de.take_weiland.mods.cameracraft.item.CCItem;
import de.take_weiland.mods.cameracraft.item.MiscItemType;
import de.take_weiland.mods.commons.meta.Subtype;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.Random;

public enum OreType implements Subtype {
	
	TIN("tin"),
	BLOCK_TIN("blockTin"),
	PHOTONIC("photonic"),
	SULFUR("sulfur");

	private final String name;
	
	OreType(String name) {
		this.name = name;
	}
	
	@Override
	public String subtypeName() {
		return name;
	}

    public void getDrops(List<ItemStack> list, Random rand, int fortune) {
        MiscItemType type;
        switch (this) {
            case PHOTONIC:
                type = MiscItemType.PHOTONIC_DUST;
                break;
            case SULFUR:
                type = MiscItemType.SULFUR_DUST;
                break;
            default:
                list.add(CCBlock.ores.getStack(this));
                return;
        }

        int amount = 2 + rand.nextInt(2 + fortune);
        for (int i = 0; i < amount; i++) {
            list.add(CCItem.misc.getStack(type));
        }
    }

}
