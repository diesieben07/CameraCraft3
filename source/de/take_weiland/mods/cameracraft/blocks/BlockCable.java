package de.take_weiland.mods.cameracraft.blocks;

import static net.minecraftforge.common.ForgeDirection.VALID_DIRECTIONS;
import net.minecraft.block.StepSound;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import de.take_weiland.mods.commons.util.Blocks;

public class BlockCable extends CCBlock {

	private int renderId;
	private Icon iconBare;
	
	protected BlockCable(int defaultId) {
		super("cable", defaultId, Material.cloth);
		setStepSound(new StepSound("stone", 1, 1) {

			@Override
			public String getPlaceSound() {
				return getStepSound();
			}

			@Override
			public String getBreakSound() {
				return getStepSound();
			}
			
		});
		
		setBlockBounds(0.2f, 0.2f, 0.2f, 0.8f, 0.8f, 0.8f);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		return AxisAlignedBB.getBoundingBox(x + 0.2, y + 0.2, z + 0.2, x + 0.8, y + 0.5, z + 0.8);
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public boolean isBlockNormalCube(World world, int x, int y, int z) {
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	public void injectRenderId(int renderId) {
		this.renderId = renderId;
	}

	@Override
	public int getRenderType() {
		return renderId;
	}

	@Override
	public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side) {
		boolean hasOneConnection = false;
		boolean[] tempSideExistsCheck = new boolean[6];
		
		for (int i = 0; i < 6; i++) {
			ForgeDirection dir = VALID_DIRECTIONS[i];
			boolean conn = connectsTo(world, x, y, z, dir);
			tempSideExistsCheck[i] = conn;
			if (hasOneConnection && conn) {
				return iconBare; // we have two connections
			}
			hasOneConnection |= conn;
		}
		
		if (hasOneConnection) {
			ForgeDirection sideDir = ForgeDirection.getOrientation(side);
			return tempSideExistsCheck[sideDir.ordinal()] || tempSideExistsCheck[sideDir.getOpposite().ordinal()] ? blockIcon : iconBare;
		} else {
			return blockIcon;
		}
	}
	
	public static boolean connectsTo(IBlockAccess world, int origX, int origY, int origZ, ForgeDirection dir) {
		return world.getBlockId(dir.offsetX + origX, origY + dir.offsetY, dir.offsetZ + origZ) == CCBlock.cable.blockID;
	}

	@Override
	public void registerIcons(IconRegister register) {
		blockIcon = Blocks.registerIcon(this, register, "idle");
		iconBare = Blocks.registerIcon(this, register, "bare");
	}

}
