package de.take_weiland.mods.cameracraft.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import de.take_weiland.mods.cameracraft.gui.CCGuis;
import de.take_weiland.mods.cameracraft.network.PacketTakePhoto;
import de.take_weiland.mods.commons.util.Sides;

public class ItemCamera extends CCItemMultitype<CameraType> {

	public ItemCamera(int defaultId) {
		super("camera", defaultId);
	}

	@Override
	public CameraType[] getTypes() {
		return CameraType.values();
	}

	@Override
	public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player) {
		if (player.isSneaking()) {
			CCGuis.CAMERA.open(player);
		} else if (Sides.logical(world).isServer()) {
			new PacketTakePhoto().sendTo(player);
		}
		
		return item;
	}

}
