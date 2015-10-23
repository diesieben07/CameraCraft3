package de.take_weiland.mods.cameracraft.item;

import de.take_weiland.mods.cameracraft.gui.CCGuis;
import de.take_weiland.mods.cameracraft.inv.InventoryCamera;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.function.Consumer;

/**
* @author diesieben07
*/
public class InventoryCameraImpl extends InventoryCamera {

    private final CameraType type;

    protected InventoryCameraImpl(int size, Consumer<ItemStack> stackCallback, EntityPlayer player, CameraType type) {
        super(size, stackCallback, player);
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
    public void setLidState(boolean close) {

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

    @Override
    public void openGui(EntityPlayer player) {
        CCGuis.CAMERA.open(player);
    }

    @Override
    public boolean takePhoto() {
        return false;
    }
}
