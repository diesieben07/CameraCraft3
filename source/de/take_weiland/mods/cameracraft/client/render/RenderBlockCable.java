package de.take_weiland.mods.cameracraft.client.render;

import static net.minecraftforge.common.ForgeDirection.VALID_DIRECTIONS;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import de.take_weiland.mods.cameracraft.blocks.BlockCable;
import de.take_weiland.mods.commons.client.Rendering;

public final class RenderBlockCable implements ISimpleBlockRenderingHandler {

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
		setRenderBoundsForCenter(renderer);
		
		Rendering.drawInventoryBlock(block, renderer, metadata);
	}

	private static void setRenderBoundsForCenter(RenderBlocks renderer) {
		renderer.setRenderBounds(0.3125f, 0.3125f, 0.3125f, 0.6875f, 0.6875f, 0.6875f);
	}
	
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		setRenderBoundsForCenter(renderer);
		renderer.renderStandardBlock(block, x, y, z);
		
		for (int i = 0; i < VALID_DIRECTIONS.length; ++i) {
			ForgeDirection dir = VALID_DIRECTIONS[i];
			if (BlockCable.connectsTo(world, x, y, z, dir)) {
				renderer.setRenderBounds(min(dir.offsetX), min(dir.offsetY), min(dir.offsetZ), max(dir.offsetX), max(dir.offsetY), max(dir.offsetZ));
				renderer.renderStandardBlock(block, x, y, z);
			}
		}
		
		return true;
	}
	
	public static float min(int dirOffset) {
		return dirOffset == 1 ? 0.6875f : dirOffset == -1 ? 0 : 0.3125f;
	}
	
	public static float max(int dirOffset) {
		return dirOffset == 1 ? 1 : dirOffset == -1 ? 0.3125f : 0.6875f;
	}

	@Override
	public boolean shouldRender3DInInventory() {
		return true;
	}

	@Override
	public int getRenderId() {
		return -1;
	}

}
