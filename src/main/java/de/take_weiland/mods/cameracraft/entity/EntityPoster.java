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

public class EntityPoster extends EntityPaintable implements IEntityAdditionalSpawnData {
	
	public EntityPoster(World world) {
		super(world);
	}

	public EntityPoster(World world, int x, int y, int z, int dir, ItemStack stack) {
		super(world, x, y, z, dir, stack);
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
}
