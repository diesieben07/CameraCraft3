package de.take_weiland.mods.cameracraft.blocks;

import net.minecraft.block.Block;
import de.take_weiland.mods.commons.templates.Metadata.BlockMeta;

public enum OreType implements BlockMeta {
	
	TIN("tin"),
	ALKALINE("alkaline"),
	BLOCK_TIN("blockTin");

	private final String name;
	
	private OreType(String name) {
		this.name = name;
	}
	
	@Override
	public String unlocalizedName() {
		return name;
	}

	@Override
	public Block getBlock() {
		return CCBlock.ores;
	}
	
}
