package de.take_weiland.mods.cameracraft.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import de.take_weiland.mods.cameracraft.inv.InventoryCamera;
import de.take_weiland.mods.commons.templates.Type;
import de.take_weiland.mods.commons.templates.Typed;
import de.take_weiland.mods.commons.util.Multitypes;

public enum CameraType implements Type<CameraType> {

	FILM("film", 2),
	DIGITAL("digital", 3);

	private final String name;
	final int slotCount;
	
	private CameraType(String name, int slotCount) {
		this.name = name;
		this.slotCount = slotCount;
	}
	
	public InventoryCamera newInventory(EntityPlayer player) {
		return new CameraInvImpl(player);
	}
	
	@Override
	public String unlocalizedName() {
		return name;
	}

	@Override
	public Typed<CameraType> getTyped() {
		return CCItem.camera;
	}
	
	@Override
	public ItemStack stack() {
		return stack(1);
	}

	@Override
	public ItemStack stack(int quantity) {
		return Multitypes.stack(this, quantity);
	}

	@Override
	public ItemStack stack(int quantity, int meta) {
		throw new IllegalArgumentException();
	}
	
	private class CameraInvImpl extends InventoryCamera {

		protected CameraInvImpl(EntityPlayer player) {
			super(player);
		}

		@Override
		public int getSizeInventory() {
			return CameraType.this.slotCount;
		}

		@Override
		public CameraType getType() {
			return CameraType.this;
		}
		
	}

}
