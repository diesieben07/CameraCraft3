package de.take_weiland.mods.cameracraft.api;

import net.minecraft.world.IBlockAccess;
import net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType;
import de.take_weiland.mods.cameracraft.api.cable.CableType;

public interface CameraCraftApi {
	
	CableType getCableType(IBlockAccess world, int x, int y, int z);
	
	EventType getTinMinableType();
	
}
