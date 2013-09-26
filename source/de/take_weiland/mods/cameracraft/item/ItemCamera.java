package de.take_weiland.mods.cameracraft.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import de.take_weiland.mods.cameracraft.gui.CCGuis;
import de.take_weiland.mods.cameracraft.inv.InventoryCamera;
import de.take_weiland.mods.cameracraft.network.PacketClientAction;
import de.take_weiland.mods.cameracraft.network.PacketClientAction.Action;
import de.take_weiland.mods.commons.util.Multitypes;
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
		} else {
			if (Sides.logical(world).isServer()) {
				InventoryCamera inv = newInventory(player);
				if (inv.hasStorageItem() && !inv.getPhotoStorage().isFull()) {
					new PacketClientAction(Action.TAKE_PHOTO).sendTo(player);
					world.playSoundAtEntity(player, "cameracraft:cameraclick", 1, 1);
				}
			}
		}
		
		return item;
	}

	public InventoryCamera newInventory(EntityPlayer player) {
		return newInventory(player, player.getCurrentEquippedItem());
	}
	
	public InventoryCamera newInventory(EntityPlayer player, ItemStack stack) {
		return isCamera(stack) ? Multitypes.getType(this, stack).newInventory(player) : null;
	}
	
	public boolean isCamera(ItemStack stack) {
		return stack != null && stack.itemID == itemID;
	}

}
