package de.take_weiland.mods.cameracraft.item;

import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.cameracraft.api.CameraCraftApi;
import de.take_weiland.mods.cameracraft.api.camera.Camera;
import de.take_weiland.mods.cameracraft.api.camera.CameraItem;
import de.take_weiland.mods.cameracraft.gui.CCGuis;
import de.take_weiland.mods.cameracraft.inv.InventoryCameraImpl;
import de.take_weiland.mods.commons.meta.MetadataProperty;
import de.take_weiland.mods.commons.util.Scheduler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
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
			CCGuis.CAMERA.open(player, (int)player.posX, (int)player.posY, (int)player.posZ);
		} else {
            final Camera inv = createCamera(player);
            if (inv.canTakePhoto()) {
                inv.onTakePhoto();

                CameraCraftApi.get().defaultTakePhoto(player, inv.getFilter())
                        .whenCompleteAsync((photoId, x) -> {
                            Throwable ex = null;
                            if (x != null) {
                                ex = x;
                            } else {
                                try {
                                    inv.getPhotoStorage().store(photoId);
                                } catch (Throwable t) {
                                    ex = t;
                                }
                            }
                            try {
                                inv.dispose();
                            } catch (Throwable t) {
                                ex = t;
                            }
                            if (ex != null) {
                                CameraCraft.printErrorMessage(player, "Failed to write image for photoID " + photoId, ex);
                            }
                        }, Scheduler.server());
            }
        }
		
		return stack;
	}

    @Override
    public Camera createCamera(EntityPlayer player, ItemStack stack, Consumer<ItemStack> stackSetter, World world, double x, double y, double z) {
        return new InventoryCameraImpl.WithPlayer(player, getType(stack), stack, stackSetter, world, Vec3.createVectorHelper(x, y, z));
    }

    @Override
    public Camera createCamera(ItemStack stack, Consumer<ItemStack> stackSetter, World world, double x, double y, double z) {
        return new InventoryCameraImpl(getType(stack), stack, stackSetter, world, Vec3.createVectorHelper(x, y, z));
    }

	public boolean isCamera(ItemStack stack) {
		return stack != null && stack.getItem() == this;
	}

}
