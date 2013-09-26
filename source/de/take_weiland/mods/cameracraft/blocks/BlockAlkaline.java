package de.take_weiland.mods.cameracraft.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraftforge.fluids.BlockFluidClassic;

public class BlockAlkaline extends BlockFluidClassic { // damn you java, we need multiple inheritance :D

	private static final String BASE_NAME = "alkaline";
	
	private Icon iconFlow;
	
	public BlockAlkaline(int defaultId) {
		super(CCBlock.getId(BASE_NAME, defaultId), CCBlock.alkalineFluid, Material.water);
	}
	
	void lateInit() {
		CCBlock.lateInitBlock(this, BASE_NAME);
	}

	@Override
	public Icon getIcon(int side, int meta) {
		return meta > 0 ? iconFlow : blockIcon;
	}

	@Override
	public void registerIcons(IconRegister register) {
		blockIcon = register.registerIcon("cameracraft:alkaline");
		iconFlow = register.registerIcon("cameracraft:alkaline_flowing");
	}


}
