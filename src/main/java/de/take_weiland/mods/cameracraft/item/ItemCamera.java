package de.take_weiland.mods.cameracraft.item;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import de.take_weiland.mods.cameracraft.CCPlayerData;
import de.take_weiland.mods.cameracraft.CCSounds;
import de.take_weiland.mods.cameracraft.api.camera.CameraInventory;
import de.take_weiland.mods.cameracraft.api.camera.CameraItem;
import de.take_weiland.mods.cameracraft.gui.CCGuis;
import de.take_weiland.mods.cameracraft.img.ImageUtil;
import de.take_weiland.mods.cameracraft.inv.InventoryCamera;
import de.take_weiland.mods.cameracraft.photo.PhotoManager;
import de.take_weiland.mods.commons.meta.MetaProperties;
import de.take_weiland.mods.commons.meta.MetadataProperty;
import de.take_weiland.mods.commons.util.Scheduler;
import de.take_weiland.mods.commons.util.Sides;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.awt.image.BufferedImage;

public class ItemCamera extends CCItemMultitype<CameraType> implements CameraItem {

    private static final MetadataProperty<CameraType> subtypeProp = MetaProperties.newProperty(0, CameraType.class);

	public ItemCamera(int defaultId) {
		super("camera", defaultId);
	}

    @Override
    public MetadataProperty<CameraType> subtypeProperty() {
        return subtypeProp;
    }

    @Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		return false;
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
					final ListenableFuture<BufferedImage> futureImage = CCPlayerData.get(player).requestStandardPhoto();
					
					CCSounds.CAMERA_CLICK.playAt(player);
					futureImage.addListener(new Runnable() {

						@Override
						public void run() {
							Integer photoId;
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
		return null;
	}
	
	public InventoryCamera newInventory(EntityPlayer player, ItemStack stack) {
		return isCamera(stack) ? subtypeProp.value(stack).newInventory(player) : null;
	}
	
	public boolean isCamera(ItemStack stack) {
		return stack != null && stack.itemID == itemID;
	}

}
