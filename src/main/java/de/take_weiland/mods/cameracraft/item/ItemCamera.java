package de.take_weiland.mods.cameracraft.item;

import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.cameracraft.api.CameraCraftApi;
import de.take_weiland.mods.cameracraft.api.camera.Camera;
import de.take_weiland.mods.cameracraft.api.camera.CameraItem;
import de.take_weiland.mods.cameracraft.gui.CCGuis;
import de.take_weiland.mods.cameracraft.inv.InventoryCamera;
import de.take_weiland.mods.commons.meta.MetadataProperty;
import de.take_weiland.mods.commons.util.Scheduler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.function.Consumer;

import static de.take_weiland.mods.commons.util.Sides.sideOf;

public class ItemCamera extends CCItemMultitype<CameraType> implements CameraItem {

    private static final MetadataProperty<CameraType> subtypeProp = MetadataProperty.newProperty(0, CameraType.class);

	public ItemCamera() {
		super("camera");
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
	public ItemStack onItemRightClick(ItemStack stack, final World world, final EntityPlayer player) {
		if (sideOf(world).isClient()) {
            return stack;
        }
		if (player.isSneaking()) {
			CCGuis.CAMERA.open(player);
		} else {
            final Camera inv = createCamera(player);
            if (inv.canTakePhoto()) {
                inv.onTakePhoto();

                CameraCraftApi.get().defaultTakePhoto(player, inv.getFilter())
                        .whenCompleteAsync((photoId, x) -> {
                            if (x != null) {
                                CameraCraft.printErrorMessage(player, "Failed to write image for photoID " + photoId, x);
                            } else {
                                inv.getPhotoStorage().store(photoId);
                            }
                            inv.dispose();
                        }, Scheduler.server());
            }
        }
		
		return stack;
	}

	@Override
	public Camera createCamera(ItemStack stack, Consumer<ItemStack> stackSetter) {
		return createCameraInternal(null); // TODO
	}

	public InventoryCamera createCameraInternal(EntityPlayer player) {
		ItemStack stack = player.getCurrentEquippedItem();
        if (isCamera(stack)) {
            return getType(stack).newInventory(player);
        } else {
            return null;
        }
	}

	public InventoryCamera newInventory(EntityPlayer player, ItemStack stack) {
		return isCamera(stack) ? getType(stack).newInventory(player) : null;
	}
	
	public boolean isCamera(ItemStack stack) {
		return stack != null && stack.getItem() == this;
	}

}
