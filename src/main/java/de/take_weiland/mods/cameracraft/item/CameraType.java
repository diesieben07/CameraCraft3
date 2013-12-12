package de.take_weiland.mods.cameracraft.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import de.take_weiland.mods.cameracraft.inv.InventoryCamera;
import de.take_weiland.mods.commons.templates.Metadata.ItemMeta;
import de.take_weiland.mods.commons.util.ItemStacks;

public enum CameraType implements ItemMeta {

	FILM("film", 2),
	DIGITAL("digital", 3);

	private final String name;
	final int slotCount;
	private final ResourceLocation texture;
	
	private CameraType(String name, int slotCount) {
		this.name = name;
		this.slotCount = slotCount;
		texture = new ResourceLocation("cameracraft", "textures/gui/" + name + "Camera.png");
	}
	
	public InventoryCamera newInventory(EntityPlayer player) {
		return this == FILM ? new InventoryCameraImpl(player) : new InventoryCameraImpl(player) {

			@Override
			public boolean canTakePhoto() {
				return super.canTakePhoto(); // TODO batteries
			}
			
		};
	}
	
	public class InventoryCameraImpl extends InventoryCamera {
		
		InventoryCameraImpl(EntityPlayer player) {
			super(player);
		}

		@Override
		public int getSizeInventory() {
			return slotCount;
		}

		@Override
		public CameraType getType() {
			return CameraType.this;
		}

		@Override
		public int storageSlot() {
			return CameraType.this == DIGITAL ? 2 : 1;
		}

		@Override
		public boolean hasLid() {
			return CameraType.this == FILM;
		}

		@Override
		public boolean canRewind() {
			return CameraType.this == FILM && storage[storageSlot()] != null;
		}

		@Override
		public boolean needsBattery() {
			return CameraType.this == DIGITAL;
		}

		@Override
		public int batterySlot() {
			return CameraType.this == DIGITAL ? 1 : -1;
		}
		
	}
	
//	public boolean isValidStorage(ItemStack stack) {
//		switch (this) {
//		case DIGITAL:
//			return PhotoStorageType.MEMORY_CARD.isThis(stack);
//		case FILM:
//			return Multitypes.is
//		}
//	}
	
	public boolean isItemValid(int slot, ItemStack stack) {
		switch (this) {
		case DIGITAL:
			return slot == 1 ? ItemStacks.is(stack, CCItem.battery) : ItemStacks.is(stack, PhotoStorageType.MEMORY_CARD);
		case FILM:
			return ItemStacks.isAny(stack, PhotoStorageType.FILM_B_W, PhotoStorageType.FILM_COLOR);
		default:
			throw new AssertionError();
		}
	}
	
	public ResourceLocation getGuiTexture() {
		return texture;
	}
	
	@Override
	public String unlocalizedName() {
		return name;
	}

	@Override
	public Item getItem() {
		return CCItem.camera;
	}
	
}
