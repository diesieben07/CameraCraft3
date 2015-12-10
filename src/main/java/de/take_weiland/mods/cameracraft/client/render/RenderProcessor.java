package de.take_weiland.mods.cameracraft.client.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import de.take_weiland.mods.cameracraft.blocks.CCRotatedBlock;
import de.take_weiland.mods.cameracraft.tileentity.TilePhotoProcessor;
import de.take_weiland.mods.commons.client.Rendering;
import de.take_weiland.mods.commons.client.icon.RotatedDirection;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

/**
 * @author diesieben07
 */
public class RenderProcessor implements ISimpleBlockRenderingHandler {

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        TilePhotoProcessor tile = (TilePhotoProcessor) world.getTileEntity(x, y, z);
        FluidStack fluidStack = tile.tank.getFluid();

        RotatedDirection front = ((CCRotatedBlock) block).getFront(world.getBlockMetadata(x, y, z));

        if (fluidStack != null) {
            Fluid fluid = fluidStack.getFluid();
            IIcon fluidIcon = fluid.getIcon(fluidStack);

            Tessellator.instance.setColorOpaque(255, 255, 255);

            double fluidIconHeight = (double) fluidStack.amount / tile.tank.getCapacity();

            renderer.setRenderBounds(0, 0, 0, 1, fluidIconHeight, 1);
            renderer.renderFaceYPos(block, x, y, z, fluidIcon);

            double jitterCorrect = 0.001;

            switch (front.getDirection()) {
                case WEST:
                    renderer.setRenderBounds(0, 4/16d, 3/16d, 1, fluidIconHeight, 7/16d);
                    renderer.renderFaceXNeg(block, x, y, z, fluidIcon);

                    renderer.setRenderBounds(0, 4/16d, 9/16d, 1, fluidIconHeight, 13/16d);
                    renderer.renderFaceXNeg(block, x, y, z, fluidIcon);
                    break;
                case EAST:
                    renderer.setRenderBounds(0, 4/16d, 3/16d, 1, fluidIconHeight, 7/16d);
                    renderer.renderFaceXPos(block, x, y, z, fluidIcon);

                    renderer.setRenderBounds(0, 4/16d, 9/16d, 1, fluidIconHeight, 13/16d);
                    renderer.renderFaceXPos(block, x, y, z, fluidIcon);
                    break;
                case NORTH:
                    renderer.setRenderBounds(3/16d, 4/16d, 0, 7/16d, fluidIconHeight, 1);
                    renderer.renderFaceZNeg(block, x, y, z, fluidIcon);

                    renderer.setRenderBounds(9/16d, 4/16d, 0, 13/16d, fluidIconHeight, 1);
                    renderer.renderFaceZNeg(block, x, y, z, fluidIcon);
                    break;
                case SOUTH:
                    renderer.setRenderBounds(3/16d, 4/16d, 0, 7/16d, fluidIconHeight, 1);
                    renderer.renderFaceZPos(block, x, y, z, fluidIcon);

                    renderer.setRenderBounds(9/16d, 4/16d, 0, 13/16d, fluidIconHeight, 1);
                    renderer.renderFaceZPos(block, x, y, z, fluidIcon);
                    break;

                case DOWN:
                case UP:
                    new UnsupportedOperationException("TODO").printStackTrace();

            }
            renderer.setRenderBoundsFromBlock(block);
        }

        renderer.renderStandardBlock(block, x, y, z);

        renderer.renderFromInside = true;
        renderer.setRenderAllFaces(true);
        renderer.setOverrideBlockTexture(((CCRotatedBlock) block).iconTop);
        renderer.renderStandardBlock(block, x, y, z);
        renderer.renderFromInside = false;
        renderer.setRenderAllFaces(false);
        renderer.clearOverrideBlockTexture();

        return true;
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        Rendering.drawInventoryBlock(block, metadata, renderer);
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return 0;
    }
}
