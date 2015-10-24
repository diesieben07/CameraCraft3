package de.take_weiland.mods.cameracraft.api.camera;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.function.Consumer;

/**
 * <p>An Item that can take photos.</p>
 */
public interface CameraItem {

    /**
     * <p>Create a new camera for the player's current item.</p>
     *
     * @param player the player
     * @return a new camera
     */
    default Camera createCamera(EntityPlayer player) {
        return createCamera(player, player.inventory, player.inventory.currentItem);
    }

    /**
     * <p>Create a new camera for the given slot in the given inventory.</p>
     * <p>The players's position is used as the camera's position.</p>
     * @param player the player using the Camera
     * @param inventory the inventory
     * @param slot the slot
     *
     * @return a new camera
     */
    default Camera createCamera(EntityPlayer player, IInventory inventory, int slot) {
        return createCamera(player, inventory, slot, player.worldObj, player.posX, player.posY, player.posZ);
    }

    /**
     * <p>Create a new camera for the given slot in the given inventory at the given position.</p>
     *
     * @param player the the player using the Camera
     * @param inventory the inventory
     * @param slot the slot
     * @param world the World of the camera
     * @param x the x position of the camera
     * @param y the y position of the camera
     * @param z the z position of the camera
     *
     * @return a new camera
     */
    default Camera createCamera(EntityPlayer player, IInventory inventory, int slot, World world, double x, double y, double z) {
        return createCamera(player, inventory.getStackInSlot(slot), stack -> inventory.setInventorySlotContents(slot, stack), world, x, y, z);
    }

    /**
     * <p>Create a new camera for the given ItemStack.</p>
     * <p>The {@code Consumer} will be called back with a possibly new ItemStack when it changes.</p>
     * <p>If no player is interacting with the camera, use {@link #createCamera(ItemStack, Consumer, World, double, double, double)} instead of
     * supplying a FakePlayer!</p>
     * @param player the player using the Camera
     * @param stack the ItemStack
     * @param stackSetter a listener for changes
     * @param world the World of the camera
     * @param x the x position of the camera
     * @param y the y position of the camera
     * @param z the z position of the camera
     * @return a new camera
     */
    Camera createCamera(EntityPlayer player, ItemStack stack, Consumer<ItemStack> stackSetter, World world, double x, double y, double z);

    /**
     * <p>Create a new camera for the given ItemStack.</p>
     * <p>The {@code Consumer} will be called back with a possibly new ItemStack when it changes.</p>
     * @param stack the ItemStack
     * @param stackSetter a listener for changes
     * @param world the World of the camera
     * @param x the x position of the camera
     * @param y the y position of the camera
     * @param z the z position of the camera
     * @return a new camera
     */
    Camera createCamera(ItemStack stack, Consumer<ItemStack> stackSetter, World world, double x, double y, double z);
	
}
