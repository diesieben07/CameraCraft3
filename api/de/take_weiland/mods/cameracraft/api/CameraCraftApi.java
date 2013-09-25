package de.take_weiland.mods.cameracraft.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType;
import de.take_weiland.mods.cameracraft.api.cable.CableType;
import de.take_weiland.mods.cameracraft.api.camera.Camera;
import de.take_weiland.mods.cameracraft.api.camera.CameraType;

public interface CameraCraftApi {
	
	CableType getCableType(IBlockAccess world, int x, int y, int z);
	
	EventType getTinMinableType();
	
	Camera getCamera(EntityPlayer player, ItemStack stack);
	
	boolean isCamera(ItemStack stack);
	
	CameraType getDigitalCamera();
	
	CameraType getFilmCamera();
	
	CableType getPowerCable();
	
	CableType getDataCable();
	
}
