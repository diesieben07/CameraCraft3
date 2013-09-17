package de.take_weiland.mods.cameracraft.inv;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import de.take_weiland.mods.cameracraft.item.CameraType;
import de.take_weiland.mods.commons.templates.ItemInventory;
import de.take_weiland.mods.commons.util.Multitypes;

public abstract class InventoryCamera extends ItemInventory.WithPlayer {

	private static final String NBT_KEY = "cameracraft.camerainv";
	
	protected InventoryCamera(EntityPlayer player) {
		super(player, NBT_KEY);
	}
	
	public abstract CameraType getType();

	@Override
	public String getInvName() {
		return Multitypes.name(getType());
	}

	@Override
	public boolean isInvNameLocalized() {
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack item) {
		// TODO Auto-generated method stub
		return super.isItemValidForSlot(slot, item);
	}
	
}
