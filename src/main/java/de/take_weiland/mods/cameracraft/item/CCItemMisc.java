package de.take_weiland.mods.cameracraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.cameracraft.api.printer.InkItem;
import de.take_weiland.mods.cameracraft.blocks.CCBlock;
import de.take_weiland.mods.commons.util.ItemStacks;
import de.take_weiland.mods.commons.util.JavaUtils;
import de.take_weiland.mods.commons.util.Multitypes;
import de.take_weiland.mods.commons.util.Sides;
import de.take_weiland.mods.commons.util.UnsignedShorts;

public class CCItemMisc extends CCItemMultitype<MiscItemType> implements InkItem {

	public CCItemMisc(int defaultId) {
		super("misc", defaultId);
	}

	@Override
	public MiscItemType[] getTypes() {
		return JavaUtils.getEnumConstantsShared(MiscItemType.class);
	}
	
	@Override
	public int getDisplayDamage(ItemStack stack) {
		return isInk(stack) ? getAmount0(stack) : 0;
	}

	@Override
	public boolean isDamaged(ItemStack stack) {
		return isInk(stack);
	}

	@Override
	public int getMaxDamage(ItemStack stack) {
		return isInk(stack) ? 100 : 0;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (Multitypes.getType(this, stack) == MiscItemType.ALKALINE_BUCKET) {
			MovingObjectPosition hit = getMovingObjectPositionFromPlayer(world, player, false);

			if (hit != null && hit.typeOfHit == EnumMovingObjectType.TILE) {
				int x = hit.blockX;
				int y = hit.blockY;
				int z = hit.blockZ;

				if (!world.canMineBlock(player, x, y, z)) {
					return stack;
				}
				
				switch (hit.sideHit) {
				case 0:
					--y;
					break;
				case 1:
					++y;
					break;
				case 2:
					--z;
					break;
				case 3:
					++z;
					break;
				case 4:
					--x;
					break;
				case 5:
					++x;
					break;
				}
				
				if (!player.canPlayerEdit(x, y, z, hit.sideHit, stack)) {
					return stack;
				}

				if (placeAlkaline(world, x, y, z) && !player.capabilities.isCreativeMode) {
					return new ItemStack(Item.bucketEmpty);
				}
			}
		}
		return stack;
	}
	
	private static boolean placeAlkaline(World world, int x, int y, int z) {
		Material material = world.getBlockMaterial(x, y, z);
		boolean isSolid = material.isSolid();

		if (!world.isAirBlock(x, y, z) && isSolid) {
			return false;
		} else {
			if (Sides.logical(world).isServer() && !isSolid && !material.isLiquid()) {
				world.destroyBlock(x, y, z, true);
			}

			world.setBlock(x, y, z, CCBlock.alkaline.blockID, 0, 3);
			return true;
		}
	}
	
	@Override
	public ItemStack getContainerItemStack(ItemStack stack) {
		return Multitypes.getType(this, stack) == MiscItemType.ALKALINE_BUCKET ? ItemStacks.of(Item.bucketEmpty) : null;
	}
	
	@Override
	public int getItemStackLimit(ItemStack stack) {
		MiscItemType type = Multitypes.getType(this, stack);
		return type == MiscItemType.ALKALINE_BUCKET || type.isInk() ? 1 : 64;
	}

	@Override
	public boolean hasCustomEntity(ItemStack stack) {
		return Multitypes.getType(this, stack) == MiscItemType.ALKALINE_DUST;
	}

	@Override
	public Entity createEntity(World world, Entity loc, ItemStack stack) {
		return new AlkalineItemEntity(world, loc.posX, loc.posY, loc.posZ, ((EntityItem)loc).delayBeforeCanPickup, loc.motionX, loc.motionY, loc.motionZ, stack);
	}
	
	@Override
	public boolean onEntityItemUpdate(EntityItem entity) { // can't do this in our custom entity class, because custom item entities don't work on the client
		if (Sides.logical(entity).isClient()) {
			int x = MathHelper.floor_double(entity.posX);
			int y = MathHelper.floor_double(entity.posY);
			int z = MathHelper.floor_double(entity.posZ);
			
			if (entity.worldObj.getBlockId(x, y, z) == Block.waterStill.blockID && itemRand.nextInt(5) == 0) {
				float xRand = (itemRand.nextFloat() - 0.5f) * 0.6f;
				float yRand = itemRand.nextFloat() * 0.5f;
				float zRand = (itemRand.nextFloat() - 0.5f) * 0.6f;
				double motionX = (itemRand.nextDouble() - 0.5) * 0.3;
				double motionY = itemRand.nextDouble() * 0.05;
				double motionZ = (itemRand.nextDouble() - 0.5) * 0.3;
				CameraCraft.env.spawnAlkalineBubbleFX(x + 0.5 + xRand, y + 1 + yRand, z + 0.5 + zRand, motionX, motionY, motionZ);
			}
		}
		return false;
	}

	public static class AlkalineItemEntity extends EntityItem {

		private int inWaterTimer = 0;
		
		AlkalineItemEntity(World world, double x, double y, double z, int pickupDelay, double motionX, double motionY, double motionZ, ItemStack stack) {
			super(world, x, y, z, stack);
			this.motionX = motionX;
			this.motionY = motionY;
			this.motionZ = motionZ;
			delayBeforeCanPickup = pickupDelay;
		}

		public AlkalineItemEntity(World world) {
			super(world);
		}

		@Override
		public void onUpdate() {
			super.onUpdate();
			if (Sides.logical(this).isServer()) {
				int x = MathHelper.floor_double(posX);
				int y = MathHelper.floor_double(posY);
				int z = MathHelper.floor_double(posZ);
				
				if (worldObj.getBlockId(x, y, z) == Block.waterStill.blockID) {
					if (inWaterTimer++ > 200) {
						setDead();
						worldObj.setBlock(x, y, z, CCBlock.alkaline.blockID);
					}
				}
			}
		}
		
	}

	// InkItem
	@Override
	public boolean isInk(ItemStack stack) {
		return Multitypes.getType(this, stack).isInk();
	}

	@Override
	public int getAmount(ItemStack stack) {
		if (isInk(stack)) {
			return 100 - getAmount0(stack);
		} else {
			return -1;
		}
	}

	private int getAmount0(ItemStack stack) {
		return UnsignedShorts.toInt(ItemStacks.getNbt(stack).getShort("cameracraft_ink"));
	}

	@Override
	public void setAmount(ItemStack stack, int newAmount) {
		if (isInk(stack)) {
			ItemStacks.getNbt(stack).setShort("cameracraft_ink", UnsignedShorts.checkedCast(100 - newAmount));
		}
	}

	@Override
	public InkItem.Color getColor(ItemStack stack) {
		return Multitypes.getType(this, stack).getInkColor();
	}

}
