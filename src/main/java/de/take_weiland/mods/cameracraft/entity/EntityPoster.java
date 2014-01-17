package de.take_weiland.mods.cameracraft.entity;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import de.take_weiland.mods.cameracraft.photo.PhotoManager;
import de.take_weiland.mods.commons.util.ItemStacks;

public class EntityPoster extends EntityHanging implements IEntityAdditionalSpawnData {

	private String photoId;
	
	public EntityPoster(World world) {
		super(world);
	}

	public EntityPoster(World world, int x, int y, int z, int dir, String photoId) {
		super(world, x, y, z, dir);
		this.photoId = photoId;
		setDirection(dir);
	}

	@Override
	public int getWidthPixels() {
		return 64;
	}

	@Override
	public int getHeightPixels() {
		return 64;
	}

	@Override
	public void onBroken(Entity entity) {
		if (entity instanceof EntityPlayer && ((EntityPlayer) entity).capabilities.isCreativeMode) {
			return;
		}
		entityDropItem(ItemStacks.of(Item.appleGold), 0);
	}

	public String getPhotoId() {
		return photoId;
	}

	@Override
	public void writeSpawnData(ByteArrayDataOutput out) {
		out.writeInt(PhotoManager.asInt(photoId));
	}

	@Override
	public void readSpawnData(ByteArrayDataInput in) {
		photoId = PhotoManager.asString(in.readInt());
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
		nbt.setInteger("photoId", PhotoManager.asInt(photoId));
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
		photoId = PhotoManager.asString(nbt.getInteger("photoId"));
	}

}
