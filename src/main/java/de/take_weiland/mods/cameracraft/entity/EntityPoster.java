package de.take_weiland.mods.cameracraft.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.cameracraft.api.photo.PhotoItem;
import de.take_weiland.mods.cameracraft.photo.PhotoManager;
import de.take_weiland.mods.commons.util.NBT;

public class EntityPoster extends EntityHanging implements IEntityAdditionalSpawnData {

	private Integer photoId;
	private ItemStack stack;
	
	public EntityPoster(World world) {
		super(world);
	}

	public EntityPoster(World world, int x, int y, int z, int dir, ItemStack stack) {
		super(world, x, y, z, dir);
		this.photoId = ((PhotoItem)stack.getItem()).getPhotoId(stack);
		this.stack = stack;
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
		entityDropItem(stack, 0);
	}

	public Integer getPhotoId() {
		return photoId;
	}

	@Override
	public boolean onValidSurface() {
		return super.onValidSurface() && photoId != null;
	}

	@Override
	public void writeSpawnData(ByteArrayDataOutput out) {
		out.writeInt(photoId.intValue());
		out.writeInt(xPosition);
		out.writeInt(yPosition);
		out.writeInt(zPosition);
		out.writeByte(hangingDirection);
	}

	@Override
	public void readSpawnData(ByteArrayDataInput in) {
		photoId = Integer.valueOf(in.readInt());
		xPosition = in.readInt();
		yPosition = in.readInt();
		zPosition = in.readInt(); // setDirection needs these
		setDirection(in.readByte());
		
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
		stack.writeToNBT(NBT.getOrCreateCompound(nbt, "photoStack"));
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
		stack = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("photoStack"));
		Item item = stack != null ? stack.getItem() : null;
		if (item instanceof PhotoItem) {
			photoId = ((PhotoItem)item).getPhotoId(stack);
		} else {
			CameraCraft.logger.warning("Invalid Item in EntityPoster at " + posX + ", " + posY + ", " + posZ);
		}
	}

}
