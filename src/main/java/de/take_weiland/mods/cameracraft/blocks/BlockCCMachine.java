package de.take_weiland.mods.cameracraft.blocks;

import de.take_weiland.mods.cameracraft.CameraCraft;
import de.take_weiland.mods.cameracraft.tileentity.TileCamera;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockCCMachine extends CCRotatedBlock {

	private final MachineType type;

	public BlockCCMachine(MachineType type) {
		super("machines." + type.subtypeName(), Material.iron);
		this.type = type;
	}

    @Override
    public int getRenderType() {
        return type == MachineType.PHOTO_PROCESSOR ? CameraCraft.proxy.getProcessorRenderId() : 0;
    }

    @Override
	public boolean hasTileEntity(int meta) {
		return type.hasTileEntity();
	}

	@Override
	public TileEntity createTileEntity(World world, int meta) {
		return type.createTileEntity();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		type.openGui(player, x, y, z);
		return true;
	}

    @Override
    public boolean getUseNeighborBrightness() {
        return type == MachineType.PHOTO_PROCESSOR;
    }

    //    @Override
//    public boolean renderAsNormalBlock() {
//        return type != MachineType.PHOTO_PROCESSOR;
//    }
//
//    @Override
//    public boolean isNormalCube() {
//        return type != MachineType.PHOTO_PROCESSOR;
//    }
//
//    @Override
//    public boolean isOpaqueCube() {
//        return type != MachineType.PHOTO_PROCESSOR;
//    }

    @Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		if (type == MachineType.CAMERA) {
			boolean isPowered = world.isBlockIndirectlyGettingPowered(x, y, z);
			TileCamera tile = (TileCamera) world.getTileEntity(x, y, z);
			if (tile.isPowered() != isPowered) {
				tile.onPowerStateChange(isPowered);
			}
		}
	}

    @Override
    protected String textureSides() {
        return "machines.generic.sides";
    }

    @Override
    protected String textureTop() {
        return "machines.generic.top";
    }
}
