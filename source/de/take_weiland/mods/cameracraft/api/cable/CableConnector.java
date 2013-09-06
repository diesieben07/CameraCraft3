package de.take_weiland.mods.cameracraft.api.cable;

import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeDirection;

public interface CableConnector {
	
	boolean canConnect(IBlockAccess world, int x, int y, int z, ForgeDirection side, CableType type);

}
