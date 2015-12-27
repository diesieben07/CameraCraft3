package de.take_weiland.mods.cameracraft.network;

import de.take_weiland.mods.cameracraft.api.photo.PhotoStorage;
import de.take_weiland.mods.cameracraft.item.ItemPhotoStorages;
import de.take_weiland.mods.commons.net.MCDataInput;
import de.take_weiland.mods.commons.net.MCDataOutput;
import de.take_weiland.mods.commons.net.Packet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * @author Intektor
 */
public class PacketMemoryHandlerDeletePicture implements Packet {

    int index;
    int containerID;
    int side;

    public PacketMemoryHandlerDeletePicture(int index, int containerID, int side) {
        this.containerID = containerID;
        this.index = index;
        this.side = side;
    }

    public PacketMemoryHandlerDeletePicture(MCDataInput in) {
        index = in.readInt();
        containerID = in.readInt();
        side = in.readInt();
    }

    @Override
    public void writeTo(MCDataOutput out) {
        out.writeInt(index);
        out.writeInt(containerID);
        out.writeInt(side);
    }

    public void handle(EntityPlayer player) {
        if(player.openContainer.windowId == containerID) {
            Slot slot = (Slot)player.openContainer.inventorySlots.get(side);
            ItemStack stack = slot.getStack();
            ItemPhotoStorages itemStorage = (ItemPhotoStorages) stack.getItem();
            PhotoStorage storage = itemStorage.getPhotoStorage(stack);
            storage.remove(index);
        }
    }
}
