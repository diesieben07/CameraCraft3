package de.take_weiland.mods.cameracraft.entity;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.cameracraft.api.photo.PhotoItem;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityPoster extends EntityHanging implements IEntityAdditionalSpawnData {

	private long photoId;
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

	public long getPhotoId() {
		return photoId;
	}

	@Override
	public void writeSpawnData(ByteBuf out) {
		out.writeLong(photoId);
		out.writeInt(field_146063_b);
		out.writeInt(field_146064_c);
		out.writeInt(field_146062_d);
		out.writeByte(hangingDirection);
	}

	@Override
	public void readSpawnData(ByteBuf in) {
		photoId = in.readLong();
		field_146063_b = in.readInt();
		field_146064_c = in.readInt();
		field_146062_d = in.readInt(); // setDirection needs these
		setDirection(in.readByte());
		
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
