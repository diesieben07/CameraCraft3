package dev.weiland.mods.cameracraft.blocks

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.material.Material
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.shapes.ISelectionContext
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.util.math.shapes.VoxelShapes
import net.minecraft.world.IBlockReader
import net.minecraft.world.IWorldReader

internal class TripodBlock : Block(Properties.create(Material.IRON).notSolid()) {

    private companion object {
        val SHAPE: VoxelShape = run {
            val shapes = arrayOf(3, 2, 2, 3, 2, 2, 2)
                .runningFold(0, Int::plus)
                .zipWithNext()
                .mapIndexed { index, (fromHeight, toHeight) ->
                    makeCuboidShape(
                        index.toDouble(), fromHeight.toDouble(), index.toDouble(),
                        (16 - index).toDouble(), toHeight.toDouble(), (16 - index).toDouble()
                    )
                }
            VoxelShapes.or(VoxelShapes.empty(), *shapes.toTypedArray())
        }
    }

    override fun getCollisionShape(state: BlockState, worldIn: IBlockReader, pos: BlockPos, context: ISelectionContext): VoxelShape {
        return SHAPE
    }

    override fun isValidPosition(state: BlockState, world: IWorldReader, pos: BlockPos): Boolean {
        val down = pos.down()
        return world.getBlockState(down).isSolidSide(world, down, Direction.UP)
    }


    override fun propagatesSkylightDown(state: BlockState, reader: IBlockReader, pos: BlockPos): Boolean {
        return true
    }

    override fun getAmbientOcclusionLightValue(state: BlockState, worldIn: IBlockReader, pos: BlockPos): Float {
        return 1f
    }
}