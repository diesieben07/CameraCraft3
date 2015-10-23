package de.take_weiland.mods.cameracraft.item;

import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.cameracraft.api.printer.InkItem;
import de.take_weiland.mods.cameracraft.blocks.CCBlock;
import de.take_weiland.mods.commons.meta.MetadataProperty;
import de.take_weiland.mods.commons.util.ItemStacks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import static de.take_weiland.mods.commons.util.Sides.sideOf;

public class CCItemMisc extends CCItemMultitype<MiscItemType> implements InkItem {

    private static final MetadataProperty<MiscItemType> type = MetadataProperty.newProperty(0, MiscItemType.class);
    public static final int MAX_AMOUNT = 100;

    public CCItemMisc() {
		super("misc");
	}

    @Override
    public MetadataProperty<MiscItemType> subtypeProperty() {
        return type;
    }

    @Override
	public boolean showDurabilityBar(ItemStack stack) {
		return isInk(stack);
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		return isInk(stack) ? getAmount0(stack) / (float) MAX_AMOUNT : 0;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (getType(stack) == MiscItemType.ALKALINE_BUCKET) {
			MovingObjectPosition hit = getMovingObjectPositionFromPlayer(world, player, false);

			if (hit != null && hit.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
				int x = hit.blockX;
				int y = hit.blockY;
				int z = hit.blockZ;

				if (!world.canMineBlock(player, x, y, z)) {
					return stack;
				}

                ForgeDirection dir = ForgeDirection.getOrientation(hit.sideHit);
                x += dir.offsetX;
                y += dir.offsetY;
                z += dir.offsetZ;

				if (!player.canPlayerEdit(x, y, z, hit.sideHit, stack)) {
					return stack;
				}

				if (placeAlkaline(world, x, y, z) && !player.capabilities.isCreativeMode) {
					return new ItemStack(Items.bucket);
				}
			}
		}
		return stack;
	}
	
	private static boolean placeAlkaline(World world, int x, int y, int z) {
		Material material = world.getBlock(x, y, z).getMaterial();
		boolean isSolid = material.isSolid();

		if (!world.isAirBlock(x, y, z) && isSolid) {
			return false;
		} else {
			if (sideOf(world).isServer() && !isSolid && !material.isLiquid()) {
				world.breakBlock(x, y, z, true);
			}

			world.setBlock(x, y, z, CCBlock.alkaline, 0, 3);
			return true;
		}
	}

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return getType(stack) == MiscItemType.ALKALINE_BUCKET;
    }

    @Override
	public ItemStack getContainerItem(ItemStack stack) {
		return new ItemStack(Items.bucket);
	}
	
	@Override
	public int getItemStackLimit(ItemStack stack) {
		MiscItemType type = getType(stack);
		return type == MiscItemType.ALKALINE_BUCKET || type.isInk() ? 1 : 64;
	}

	@Override
	public boolean hasCustomEntity(ItemStack stack) {
		return getType(stack) == MiscItemType.ALKALINE_DUST;
	}

	@Override
	public Entity createEntity(World world, Entity loc, ItemStack stack) {
		return new AlkalineItemEntity(world, loc.posX, loc.posY, loc.posZ, ((EntityItem)loc).delayBeforeCanPickup, loc.motionX, loc.motionY, loc.motionZ, stack);
	}
	
	@Override
	public boolean onEntityItemUpdate(EntityItem entity) { // can't do this in our custom entity class, because custom item entities don't work on the client
		if (sideOf(entity).isClient()) {
			int x = MathHelper.floor_double(entity.posX);
			int y = MathHelper.floor_double(entity.posY);
			int z = MathHelper.floor_double(entity.posZ);
			
			if (entity.worldObj.getBlock(x, y, z) == Blocks.water && itemRand.nextInt(5) == 0) {
				float xRand = (itemRand.nextFloat() - 0.5f) * 0.6f;
				float yRand = itemRand.nextFloat() * 0.5f;
				float zRand = (itemRand.nextFloat() - 0.5f) * 0.6f;
				double motionX = (itemRand.nextDouble() - 0.5) * 0.3;
				double motionY = itemRand.nextDouble() * 0.05;
				double motionZ = (itemRand.nextDouble() - 0.5) * 0.3;
				CameraCraft.proxy.spawnAlkalineBubbleFX(x + 0.5 + xRand, y + 1 + yRand, z + 0.5 + zRand, motionX, motionY, motionZ);
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
			if (sideOf(this).isServer()) {
				int x = MathHelper.floor_double(posX);
				int y = MathHelper.floor_double(posY);
				int z = MathHelper.floor_double(posZ);
				
				if (worldObj.getBlock(x, y, z) == Blocks.water) {
					if (inWaterTimer++ > 200) {
						setDead();
						worldObj.setBlock(x, y, z, CCBlock.alkaline);
					}
				}
			}
		}
		
	}

	// InkItem
	@Override
	public boolean isInk(ItemStack stack) {
		return getType(stack).isInk();
	}

	@Override
	public int getAmount(ItemStack stack) {
		if (isInk(stack)) {
			return MAX_AMOUNT - getAmount0(stack);
		} else {
			return -1;
		}
	}

	private int getAmount0(ItemStack stack) {
		return ItemStacks.getNbt(stack).getInteger("cameracraft_ink");
	}

	@Override
	public void setAmount(ItemStack stack, int newAmount) {
		if (isInk(stack)) {
			ItemStacks.getNbt(stack).setInteger("cameracraft_ink", MAX_AMOUNT - newAmount);
		}
	}

	@Override
	public InkItem.Color getColor(ItemStack stack) {
		return getType(stack).getInkColor();
	}

}
