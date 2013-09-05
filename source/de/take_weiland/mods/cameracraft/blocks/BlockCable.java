package de.take_weiland.mods.cameracraft.blocks;

import static de.take_weiland.mods.commons.util.Blocks.BLOCK_UPDATE;
import static de.take_weiland.mods.commons.util.Blocks.UPDATE_CLIENTS;
import static net.minecraftforge.common.ForgeDirection.VALID_DIRECTIONS;
import de.take_weiland.mods.cameracraft.tileentity.TileEntityCable;
import net.minecraft.block.StepSound;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class BlockCable extends CCBlock {

	private int renderId;
	
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
	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return true || canBlockStay(world, x, y, z);
	}

	@Override
	public boolean canBlockStay(World world, int x, int y, int z) {
		return world.isBlockSolidOnSide(x, y - 1, z, ForgeDirection.UP);
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int neighbourBlockId) {
		if (!canBlockStay(world, x, y, z)) {
//			dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
//			world.setBlockToAir(x, y, z);
		} else {
			((TileEntityCable)world.getBlockTileEntity(x, y, z)).updateConnections();
		}
	}

	public void injectRenderId(int renderId) {
		this.renderId = renderId;
	}

	@Override
	public int getRenderType() {
		return renderId;
	}

	@Override
	public void setBlockBoundsForItemRender() {
		setBlockBounds(0, 0, 0, 1, 0.25f, 1);
	}

	@Override
	public boolean hasTileEntity(int metadata) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		return new TileEntityCable();
	}

}
