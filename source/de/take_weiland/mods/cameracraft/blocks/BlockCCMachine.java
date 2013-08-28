package de.take_weiland.mods.cameracraft.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import de.take_weiland.mods.commons.util.Multitypes;

public class BlockCCMachine extends CCMultitypeBlock<MachineType> {

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
		return MachineType.values();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		Multitypes.getType(this, world.getBlockMetadata(x, y, z)).openGui(player, x, y, z);
		return true;
	}

}
