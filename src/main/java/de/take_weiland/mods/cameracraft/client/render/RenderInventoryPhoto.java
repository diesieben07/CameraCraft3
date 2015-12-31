package de.take_weiland.mods.cameracraft.client.render;

import de.take_weiland.mods.cameracraft.client.PhotoDataCache;
import de.take_weiland.mods.cameracraft.item.ItemPhoto;
import de.take_weiland.mods.commons.client.Rendering;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

/**
 * @author Intektor
 */
public class RenderInventoryPhoto implements IItemRenderer {

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return type == ItemRenderType.INVENTORY || type == ItemRenderType.EQUIPPED_FIRST_PERSON;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return false;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack stack, Object... data) {
        if (type == ItemRenderType.INVENTORY) {
            if (stack != null && stack.getItem() != null && stack.getItem() instanceof ItemPhoto) {
                ItemPhoto photo = (ItemPhoto) stack.getItem();
                if (photo != null) {
                    long l = photo.getPhotoId(stack);
                    PhotoDataCache.bindTexture(l);
                    Rendering.drawTexturedQuadFit(0, 0, 16, 16, 0);
                }
            }
        }

        if (type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
            
        }
    }


}
