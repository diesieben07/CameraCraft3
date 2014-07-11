package de.take_weiland.mods.cameracraft.blocks;

import de.take_weiland.mods.cameracraft.tileentity.TileCamera;
import de.take_weiland.mods.commons.meta.MetaProperties;
import de.take_weiland.mods.commons.meta.MetadataProperty;
import de.take_weiland.mods.commons.meta.Subtypes;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockCCMachine extends CCMultitypeBlock<MachineType> {

	private static final MetadataProperty<MachineType> subtypeProp = MetaProperties.newProperty(0, MachineType.class);

	public BlockCCMachine(int defaultId) {
		super("machines", defaultId, Material.iron);
	}

	@Override
	public MetadataProperty<MachineType> subtypeProperty() {
		return subtypeProp;
	}

	@Override
	public boolean hasTileEntity(int meta) {
		return Subtypes.getType(this, meta).hasTileEntity();
	}

	@Override
	public TileEntity createTileEntity(World world, int meta) {
		return Subtypes.getType(this, meta).createTileEntity();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		Subtypes.getType(this, world.getBlockMetadata(x, y, z)).openGui(player, x, y, z);
		return true;
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int blockId) {
		if (Subtypes.getType(this, world.getBlockMetadata(x, y, z)) == MachineType.CAMERA) {
			boolean isPowered = world.isBlockIndirectlyGettingPowered(x, y, z);
			TileCamera tile = (TileCamera) world.getBlockTileEntity(x, y, z);
			if (tile.isPowered() != isPowered) {
				tile.onPowerStateChange(isPowered);
			}
		}
	}

}
