package de.take_weiland.mods.cameracraft.item;

import de.take_weiland.mods.cameracraft.inv.InventoryCamera;
import net.minecraft.entity.player.EntityPlayer;

/**
* Created by Take on 10.07.2014.
*/
public class InventoryCameraImpl extends InventoryCamera {

    private CameraType type;

    InventoryCameraImpl(CameraType type, EntityPlayer player) {
        super(type.slotCount, player);
        this.type = type;
    }

    @Override
    public CameraType getType() {
        return type;
    }

    @Override
    public int storageSlot() {
        return type == CameraType.DIGITAL ? 2 : 1;
    }

    @Override
    public boolean hasLid() {
        return type == CameraType.FILM;
    }

    @Override
    public boolean canRewind() {
        return type == CameraType.FILM && storage[storageSlot()] != null;
    }

    @Override
    public boolean needsBattery() {
        return type == CameraType.DIGITAL;
    }

    @Override
    public int batterySlot() {
        return type == CameraType.DIGITAL ? 1 : -1;
    }

}
