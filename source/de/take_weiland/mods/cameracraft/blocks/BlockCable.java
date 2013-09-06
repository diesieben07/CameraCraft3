package de.take_weiland.mods.cameracraft.blocks;

import static net.minecraftforge.common.ForgeDirection.*;
import net.minecraft.block.StepSound;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import de.take_weiland.mods.cameracraft.tileentity.TileEntityCable;
import de.take_weiland.mods.commons.util.Blocks;

public class BlockCable extends CCBlock {

	private int renderId;
	private Icon iconIdle;
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
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
		return getCollisionBoundingBoxFromPool(world, x, y, z);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		return AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 0.25, z + 1);
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
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int neighbourBlockId) {
		((TileEntityCable)world.getBlockTileEntity(x, y, z)).updateConnections();
	}

	public void injectRenderId(int renderId) {
		this.renderId = renderId;
	}

	@Override
	public int getRenderType() {
		return renderId;
	}

	@Override
	public boolean hasTileEntity(int metadata) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		return new TileEntityCable();
	}

	@Override
	public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side) {
		boolean hasOneConnection = false;
		boolean[] tempSideExistsCheck = new boolean[6];
		
		for (int i = 0; i < 6; i++) {
			ForgeDirection dir = VALID_DIRECTIONS[i];
			boolean conn = existsAt(world, x, y, z, dir);
			tempSideExistsCheck[i] = conn;
			if (hasOneConnection && conn) {
				return iconBare; // we have two connections
			}
			hasOneConnection |= conn;
		}
		
		if (hasOneConnection) {
			ForgeDirection sideDir = ForgeDirection.getOrientation(side);
			return tempSideExistsCheck[sideDir.ordinal()] || tempSideExistsCheck[sideDir.getOpposite().ordinal()] ? iconIdle : iconBare;
		} else {
			return iconIdle;
		}
	}
	
	private boolean existsAt(IBlockAccess world, int origX, int origY, int origZ, ForgeDirection dir) {
		return world.getBlockId(dir.offsetX + origX, origY + dir.offsetY, dir.offsetZ + origZ) == blockID;
	}

	@Override
	public void registerIcons(IconRegister register) {
		iconIdle = Blocks.registerIcon(this, register, "idle");
		iconBare = Blocks.registerIcon(this, register, "bare");
	}

}
