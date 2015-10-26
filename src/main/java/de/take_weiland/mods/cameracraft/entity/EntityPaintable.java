package de.take_weiland.mods.cameracraft.entity;

import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.cameracraft.api.photo.PhotoItem;
import de.take_weiland.mods.cameracraft.client.PhotoDataCache;
import de.take_weiland.mods.cameracraft.item.ItemPen;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * @author Intektor
 */
public abstract class EntityPaintable extends EntityHanging {

    protected long photoId;
    protected ItemStack stack;

    public EntityPaintable(World world) {
        super(world);
    }

    public EntityPaintable(World world, int x, int y, int z, int dir, ItemStack stack) {
        super(world, x, y, z, dir);
        this.photoId = ((PhotoItem)stack.getItem()).getPhotoId(stack);
        this.stack = stack;
        setDirection(dir);
    }

    @Override
    public boolean interactFirst(EntityPlayer player) {

        ItemStack stack = player.getCurrentEquippedItem();

        if(stack != null) {
            if(stack. getItem() instanceof ItemPen) {
                PhotoDataCache.bindTexture(photoId);
            }
        }

        return false;
    }
    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setTag("photoStack", stack.writeToNBT(new NBTTagCompound()));
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        stack = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("photoStack"));
        Item item = stack != null ? stack.getItem() : null;
        if (item instanceof PhotoItem) {
            photoId = ((PhotoItem)item).getPhotoId(stack);
        } else {
            CameraCraft.logger.warn("Invalid Item in EntityPoster at " + posX + ", " + posY + ", " + posZ);
            setDead();
        }
    }
}
