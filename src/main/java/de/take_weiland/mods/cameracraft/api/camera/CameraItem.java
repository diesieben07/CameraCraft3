package de.take_weiland.mods.cameracraft.api.camera;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

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
        return createCamera(player.inventory, player.inventory.currentItem);
    }

    /**
     * <p>Create a new camera for the given slot in the given inventory.</p>
     *
     * @return a new camera
     */
    default Camera createCamera(IInventory inventory, int slot) {
        return createCamera(inventory.getStackInSlot(slot), stack -> inventory.setInventorySlotContents(slot, stack));
    }

    /**
     * <p>Create a new camera for the given ItemStack.</p>
     * <p>The {@code Consumer} will be called back with a possibly new ItemStack when it changes.</p>
     * @param stack the ItemStack
     * @param stackSetter a listener for changes
     * @return a new camera
     */
    Camera createCamera(ItemStack stack, Consumer<ItemStack> stackSetter);
	
}
