package de.take_weiland.mods.cameracraft;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType;

import com.google.common.util.concurrent.ListenableFuture;

import de.take_weiland.mods.cameracraft.api.CameraCraftApi;
import de.take_weiland.mods.cameracraft.api.cable.CableType;
import de.take_weiland.mods.cameracraft.api.camera.CameraItem;
import de.take_weiland.mods.cameracraft.blocks.CCBlock;
import de.take_weiland.mods.cameracraft.worldgen.CCWorldGen;
import de.take_weiland.mods.commons.util.Multitypes;

final class ApiImpl implements CameraCraftApi {

	@Override
	public CableType getCableType(IBlockAccess world, int x, int y, int z) {
		return Multitypes.getType(CCBlock.cable, world.getBlockMetadata(x, y, z));
	}

	@Override
	public EventType getTinMinableType() {
		return CCWorldGen.TIN_EVENT_TYPE;
	}

	@Override
	public boolean isCamera(ItemStack stack) {
		return stack != null && stack.getItem() instanceof CameraItem;
	}

	@Override
	public CableType getPowerCable() {
		return de.take_weiland.mods.cameracraft.blocks.CableType.POWER;
	}

	@Override
	public CableType getDataCable() {
		return de.take_weiland.mods.cameracraft.blocks.CableType.DATA;
	}

	@Override
	public ListenableFuture<String> takePhoto(EntityPlayer player) {
		return null;

	}

}
