package de.take_weiland.mods.cameracraft.item;

import de.take_weiland.mods.commons.meta.Subtype;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import de.take_weiland.mods.cameracraft.inv.InventoryCamera;
import de.take_weiland.mods.commons.templates.Metadata.ItemMeta;
import de.take_weiland.mods.commons.util.ItemStacks;

public enum CameraType implements Subtype {

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
		return this == FILM ? new InventoryCameraImpl(this, player) : new InventoryCameraImpl(this, player) {

			@Override
			public boolean canTakePhoto() {
				return super.canTakePhoto(); // TODO batteries
			}
			
		};
	}

    public boolean isItemValid(int slot, ItemStack stack) {
		if (this == DIGITAL) {
			return slot == 1 ? ItemStacks.is(stack, CCItem.battery) : ItemStacks.is(stack, PhotoStorageType.MEMORY_CARD);
		} else {
			return ItemStacks.isAny(stack, PhotoStorageType.FILM_B_W, PhotoStorageType.FILM_COLOR);
		}
	}
	
	public ResourceLocation getGuiTexture() {
		return texture;
	}

    @Override
    public String subtypeName() {
        return name;
    }

}
