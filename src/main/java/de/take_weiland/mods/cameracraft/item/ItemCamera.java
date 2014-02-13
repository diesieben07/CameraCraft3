package de.take_weiland.mods.cameracraft.item;

import static de.take_weiland.mods.commons.util.Multitypes.getType;

import java.awt.image.BufferedImage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import de.take_weiland.mods.cameracraft.CCSounds;
import de.take_weiland.mods.cameracraft.PhotoRequestManager;
import de.take_weiland.mods.cameracraft.api.camera.CameraInventory;
import de.take_weiland.mods.cameracraft.api.camera.CameraItem;
import de.take_weiland.mods.cameracraft.gui.CCGuis;
import de.take_weiland.mods.cameracraft.img.ImageUtil;
import de.take_weiland.mods.cameracraft.inv.InventoryCamera;
import de.take_weiland.mods.cameracraft.photo.PhotoManager;
import de.take_weiland.mods.commons.util.Scheduler;
import de.take_weiland.mods.commons.util.Sides;

public class ItemCamera extends CCItemMultitype<CameraType> implements CameraItem {

	public ItemCamera(int defaultId) {
		super("camera", defaultId);
	}

	@Override
	public CameraType[] getTypes() {
		return CameraType.values();
	}

	@Override
	public ItemStack onItemRightClick(ItemStack item, final World world, final EntityPlayer player) {
		if (player.isSneaking()) {
			CCGuis.CAMERA.open(player);
		} else {
			if (Sides.logical(world).isServer()) {
				final CameraInventory inv = getInventory(player);
				if (inv.canTakePhoto()) {
					inv.onTakePhoto();
					final ListenableFuture<BufferedImage> futureImage = PhotoRequestManager.requestPhoto(player);
					
					CCSounds.CAMERA_CLICK.playAt(player);
					futureImage.addListener(new Runnable() {

						@Override
						public void run() {
							Integer photoId = null;
							if (!inv.getPhotoStorage().isFull()) {
								photoId = PhotoManager.nextPhotoId(world);
								
								ImageUtil.savePngAsync(Futures.getUnchecked(futureImage), PhotoManager.getImageFile(photoId), inv.getFilter());
								
								inv.getPhotoStorage().store(photoId);
								inv.dispose();
							}
						}
						
					}, Scheduler.server());
				}
			}
		}
		
		return item;
	}

	@Override
	public InventoryCamera getInventory(EntityPlayer player) {
		return newInventory(player, player.getCurrentEquippedItem());
	}
	
	@Override
	public CameraInventory getInventory(IInventory inventory, int slot) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public InventoryCamera newInventory(EntityPlayer player, ItemStack stack) {
		return isCamera(stack) ? getType(this, stack).newInventory(player) : null;
	}
	
	public boolean isCamera(ItemStack stack) {
		return stack != null && stack.itemID == itemID;
	}

}
