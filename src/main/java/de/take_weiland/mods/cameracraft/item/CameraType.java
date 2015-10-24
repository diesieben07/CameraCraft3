package de.take_weiland.mods.cameracraft.item;

import de.take_weiland.mods.commons.meta.Subtype;
import de.take_weiland.mods.commons.util.ItemStacks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public enum CameraType implements Subtype {

	FILM("film", 2),
	DIGITAL("digital", 3);

	private final String name;
	public final int slotCount;
	private final ResourceLocation texture;
	
	CameraType(String name, int slotCount) {
		this.name = name;
		this.slotCount = slotCount;
		texture = new ResourceLocation("cameracraft", "textures/gui/" + name + "Camera.png");
	}

	public boolean isItemValid(int slot, ItemStack stack) {
		if (this == DIGITAL) {
			return slot == 1
					? ItemStacks.is(stack, CCItem.battery)
					: CCItem.photoStorage.getType(stack) == PhotoStorageType.MEMORY_CARD;
		} else {
            PhotoStorageType type = CCItem.photoStorage.getType(stack);
            return type == PhotoStorageType.FILM_B_W || type == PhotoStorageType.FILM_COLOR;
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
