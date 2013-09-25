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
				new PacketClientAction(Action.TAKE_PHOTO).sendTo(player);
			}
			
			player.playSound("cameracraft:cameraclick", 1, 1);
		}
		
		return item;
	}
	
	public InventoryCamera newInventory(EntityPlayer player, ItemStack stack) {
		return isCamera(stack) ? Multitypes.getType(this, stack).newInventory(player) : null;
	}
	
	public boolean isCamera(ItemStack stack) {
		return stack != null && stack.itemID == itemID;
	}
	
//
//	public static PhotoStorage storageForItemStack(ItemStack stack) {
//		return new PhotoStorage() {
//			
//			@Override
//			public void store(Photo photo) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public int size() {
//				// TODO Auto-generated method stub
//				return 0;
//			}
//			
//			@Override
//			public List<Photo> getPhotos() {
//				// TODO Auto-generated method stub
//				return null;
//			}
//			
//			@Override
//			public int capacity() {
//				// TODO Auto-generated method stub
//				return 0;
//			}
//		};
//	}
	
}
