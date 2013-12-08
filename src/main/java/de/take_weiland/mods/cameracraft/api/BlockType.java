package de.take_weiland.mods.cameracraft.api;

import net.minecraft.world.IBlockAccess;

public interface BlockType<T extends BlockType<T>> extends ItemType<T> {

	boolean isThis(IBlockAccess world, int x, int y, int z);
	
}
