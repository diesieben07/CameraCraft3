package de.take_weiland.mods.cameracraft.blocks;

import static net.minecraftforge.common.ForgeDirection.VALID_DIRECTIONS;
import net.minecraft.block.StepSound;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.take_weiland.mods.cameracraft.api.cable.CableConnector;
import de.take_weiland.mods.cameracraft.tileentity.TileEntityDataCable;
import de.take_weiland.mods.commons.client.Icons;
import de.take_weiland.mods.commons.util.JavaUtils;
import de.take_weiland.mods.commons.util.Multitypes;

public class BlockCable extends CCMultitypeBlock<CableType> implements CableConnector {

	private int renderId = -1;
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
		setBlockBoundsForItemRender();
	}

	@Override
	public void setBlockBoundsForItemRender() {
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

	private final boolean[] sideExistCheck = new boolean[6];
	
	@Override
	public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side) {
		boolean hasOneConnection = false;
		boolean[] tempSideExistsCheck = sideExistCheck;
		
		int meta = world.getBlockMetadata(x, y, z);
		
		for (int i = 0; i < 6; i++) {
			ForgeDirection dir = VALID_DIRECTIONS[i];
			boolean conn = connectsTo(world, x, y, z, dir);
			if (hasOneConnection && conn) {
				return getBareIcon(meta); // we have at least two connections so return the "bare" texture
			}
			tempSideExistsCheck[i] = conn;
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
		return JavaUtils.get(bareIcons, meta);
	}
	
	public static boolean connectsTo(IBlockAccess world, int x, int y, int z, ForgeDirection dir) {
		int otherX = x + dir.offsetX;
		int otherY = y + dir.offsetY;
		int otherZ = z + dir.offsetZ;
		net.minecraft.block.Block block = blocksList[world.getBlockId(otherX, otherY, otherZ)];
		return block instanceof CableConnector && ((CableConnector)block).canConnect(world, otherX, otherY, otherZ, dir.getOpposite(), Multitypes.getType(cable, world.getBlockMetadata(x, y, z)));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register) {
		icons = Icons.registerMulti(register, getTypes(), "active");
		bareIcons = Icons.registerMulti(register, getTypes(), "bare");
	}

	@Override
	public boolean canConnect(IBlockAccess world, int x, int y, int z, ForgeDirection side, de.take_weiland.mods.cameracraft.api.cable.CableType type) {
		return Multitypes.getType(this, world.getBlockMetadata(x, y, z)) == type;
	}

	@Override
	public CableType[] getTypes() {
		return CableType.VALUES;
	}

	@Override
	public boolean hasTileEntity(int metadata) {
		return Multitypes.getType(this, metadata) == CableType.DATA;
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		return Multitypes.getType(this, metadata) == CableType.DATA ? new TileEntityDataCable() : null;
	}

//	@Override
//	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
//		if (Sides.logical(world).isServer()) {
//			TileEntityDataCable tile = (TileEntityDataCable) world.getBlockTileEntity(x, y, z);
//			System.out.println(tile.getNetwork().getNodes().size());
//			System.out.println(tile.getNetwork().hashCode());
//		}
//		return true;
//	}

}
