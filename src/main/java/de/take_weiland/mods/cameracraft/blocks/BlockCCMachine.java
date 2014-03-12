package de.take_weiland.mods.cameracraft.blocks;

import de.take_weiland.mods.cameracraft.api.cable.CableConnector;
import de.take_weiland.mods.cameracraft.api.cable.CableType;
import de.take_weiland.mods.cameracraft.tileentity.TileCamera;
import de.take_weiland.mods.commons.util.JavaUtils;
import de.take_weiland.mods.commons.util.Multitypes;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class BlockCCMachine extends CCMultitypeBlock<MachineType> implements CableConnector {

	public BlockCCMachine(int defaultId) {
		super("machines", defaultId, Material.iron);
	}

	@Override
	public boolean hasTileEntity(int meta) {
		return Multitypes.getType(this, meta).hasTileEntity();
	}

	@Override
	public TileEntity createTileEntity(World world, int meta) {
		return Multitypes.getType(this, meta).createTileEntity();
	}

	@Override
	public MachineType[] getTypes() {
		return JavaUtils.getEnumConstantsShared(MachineType.class);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		Multitypes.getType(this, world.getBlockMetadata(x, y, z)).openGui(player, x, y, z);
		return true;
	}

	@Override
	public boolean canConnect(IBlockAccess world, int x, int y, int z, ForgeDirection side, CableType type) {
		return Multitypes.getType(this, world.getBlockMetadata(x, y, z)).canCableConnect(side, type);
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int blockId) {
		if (Multitypes.getType(this, world.getBlockMetadata(x, y, z)) == MachineType.CAMERA) {
			boolean isPowered = world.isBlockIndirectlyGettingPowered(x, y, z);
			TileCamera tile = (TileCamera) world.getBlockTileEntity(x, y, z);
			if (tile.isPowered() != isPowered) {
				tile.onPowerStateChange(isPowered);
			}
		}
	}

}
