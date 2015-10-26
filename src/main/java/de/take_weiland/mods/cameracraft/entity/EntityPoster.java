package de.take_weiland.mods.cameracraft.entity;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.cameracraft.api.photo.PhotoItem;
import de.take_weiland.mods.cameracraft.item.ItemPen;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityPoster extends EntityPaintable {
	
	public EntityPoster(World world) {
		super(world);
	}

	public EntityPoster(World world, int x, int y, int z, int dir, ItemStack stack, int dimX, int dimY) {
		super(world, x, y, z, dir, stack, dimX, dimY);
	}

	@Override
	public void onBroken(Entity entity) {
		entityDropItem(stack, 0);
	}

	public long getPhotoId() {
		return photoId;
	}
}
