package de.take_weiland.mods.cameracraft.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import de.take_weiland.mods.cameracraft.inv.InventoryCamera;
import de.take_weiland.mods.commons.templates.Type;
import de.take_weiland.mods.commons.templates.Typed;
import de.take_weiland.mods.commons.util.Multitypes;

public enum CameraType implements Type<CameraType>, de.take_weiland.mods.cameracraft.api.camera.CameraType {

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
		return new InventoryCamera(player) {
			
			@Override
			public int getSizeInventory() {
				return slotCount;
			}

			@Override
			public CameraType getType() {
				return CameraType.this;
			}

			@Override
			protected int storageSlot() {
				return CameraType.this == DIGITAL ? 2 : 1;
			}
			
		};
	}
	
	public boolean isItemValid(int slot, ItemStack stack) {
		switch (this) {
		case DIGITAL:
			return slot == 1 ? CCItem.battery.isThis(stack) : PhotoStorageType.MEMORY_CARD.isThis(stack);
		case FILM:
			return Multitypes.isAny(stack, PhotoStorageType.FILM_B_W, PhotoStorageType.FILM_COLOR);
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
	public boolean isThis(ItemStack stack) {
		return Multitypes.is(stack, this);
	}
	
	// API interface
	@Override
	public int getSlots() {
		return slotCount;
	}

	@Override
	public boolean isDigital() {
		return this == DIGITAL;
	}

	@Override
	public boolean isFilm() {
		return this == FILM;
	}

}
