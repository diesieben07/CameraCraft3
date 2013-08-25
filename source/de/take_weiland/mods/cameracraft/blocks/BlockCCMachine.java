package de.take_weiland.mods.cameracraft.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import de.take_weiland.mods.cameracraft.CCGuis;
import de.take_weiland.mods.commons.templates.Typed;
import de.take_weiland.mods.commons.util.Items;

public class BlockCCMachine extends CCBlock implements Typed<MachineType> {

	public BlockCCMachine(int defaultId) {
		super("machines", defaultId, Material.iron);
	}

	@Override
	public boolean hasTileEntity(int meta) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, int meta) {
		return Items.getType(this, meta).createTileEntity();
	}

	@Override
	public MachineType[] getTypes() {
		return MachineType.values();
	}

	@Override
	public MachineType getDefault() {
		return MachineType.PHOTO_PROCESSOR;
	}

	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
		CCGuis.PHOTO_PROCESSOR.open(par5EntityPlayer, par2, par3, par4);
		return true;
	}

}
