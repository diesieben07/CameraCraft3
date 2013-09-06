package de.take_weiland.mods.cameracraft.blocks;

import static net.minecraftforge.common.ForgeDirection.VALID_DIRECTIONS;
import net.minecraft.block.Block;
import net.minecraft.block.StepSound;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.take_weiland.mods.cameracraft.api.cable.CableConnector;
import de.take_weiland.mods.commons.util.Blocks;
import de.take_weiland.mods.commons.util.CollectionUtils;

import static de.take_weiland.mods.commons.util.Multitypes.getType;

public class BlockCable extends CCMultitypeBlock<CableType> implements CableConnector {

	private int renderId;
	private Icon[] bareIcons;
	
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
		
		int meta = world.getBlockMetadata(x, y, z);
		
		for (int i = 0; i < 6; i++) {
			ForgeDirection dir = VALID_DIRECTIONS[i];
			boolean conn = connectsTo(world, x, y, z, dir);
			tempSideExistsCheck[i] = conn;
			if (hasOneConnection && conn) {
				return getBareIcon(meta); // we have at least two connections so return the "bare" texture
			}
			hasOneConnection |= conn;
		}
		
		if (hasOneConnection) {
			ForgeDirection sideDir = ForgeDirection.getOrientation(side);
			return tempSideExistsCheck[sideDir.ordinal()] || tempSideExistsCheck[sideDir.getOpposite().ordinal()] ? getIcon(side, meta) : getBareIcon(meta);
		} else {
			return getIcon(side, meta);
		}
	}
	
	private Icon getBareIcon(int meta) {
		return CollectionUtils.safeArrayAccess(bareIcons, meta);
	}
	
	public static boolean connectsTo(IBlockAccess world, int x, int y, int z, ForgeDirection dir) {
		int otherX = x + dir.offsetX;
		int otherY = y + dir.offsetY;
		int otherZ = z + dir.offsetZ;
		int blockId = world.getBlockId(otherX, otherY, otherZ);
		if (blockId == 0) {
			return false;
		} else {
			Block block = blocksList[blockId];
			return block instanceof CableConnector && ((CableConnector)block).canConnect(world, otherX, otherY, otherZ, dir.getOpposite(), getType(cable, world.getBlockMetadata(x, y, z)).toApiForm());
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register) {
		icons = Blocks.registerIcons(this, "active", register);
		bareIcons = Blocks.registerIcons(this, "bare", register);
	}

	@Override
	public boolean canConnect(IBlockAccess world, int x, int y, int z, ForgeDirection side, de.take_weiland.mods.cameracraft.api.cable.CableType type) {
		return getType(this, world.getBlockMetadata(x, y, z)).toApiForm() == type;
	}

	@Override
	public CableType[] getTypes() {
		return CableType.values();
	}

}
