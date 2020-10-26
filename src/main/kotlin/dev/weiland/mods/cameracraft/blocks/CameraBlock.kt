package dev.weiland.mods.cameracraft.blocks

import dev.weiland.mods.cameracraft.CCBlocks
import dev.weiland.mods.cameracraft.util.modulus
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.material.Material
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.BlockItemUseContext
import net.minecraft.state.IntegerProperty
import net.minecraft.state.StateContainer
import net.minecraft.util.ActionResultType
import net.minecraft.util.Direction
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.BlockRayTraceResult
import net.minecraft.util.math.shapes.ISelectionContext
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.world.IBlockReader
import net.minecraft.world.IWorld
import net.minecraft.world.IWorldReader
import net.minecraft.world.World
import kotlin.math.roundToInt

internal class CameraBlock : Block(Properties.create(Material.IRON).notSolid()) {

    companion object {
        val ROTATION = IntegerProperty.create("rotation", 0, 35)

        private fun makeShape(degrees: Int) {

        }


        val SHAPE = makeCuboidShape(1.0, 0.0, 1.0, 15.0, 10.0, 15.0)

    }

    override fun getShape(state: BlockState, worldIn: IBlockReader, pos: BlockPos, context: ISelectionContext): VoxelShape {
        return SHAPE
    }

    override fun getCollisionShape(state: BlockState, worldIn: IBlockReader, pos: BlockPos, context: ISelectionContext): VoxelShape {
        return SHAPE
    }

    override fun fillStateContainer(builder: StateContainer.Builder<Block, BlockState>) {
        builder.add(ROTATION)
    }

    override fun updatePostPlacement(
        state: BlockState, facing: Direction, facingState: BlockState, world: IWorld, currentPos: BlockPos, facingPos: BlockPos
    ): BlockState {
        return if (!isValidPosition(state, world, currentPos)) {
            Blocks.AIR.defaultState
        } else {
            super.updatePostPlacement(state, facing, facingState, world, currentPos, facingPos)
        }
    }

    override fun isValidPosition(state: BlockState, world: IWorldReader, pos: BlockPos): Boolean {
        return world.getBlockState(pos.down()).block == CCBlocks.TRIPOD
    }

    override fun getStateForPlacement(context: BlockItemUseContext): BlockState {
        val rotation = ((360 - context.placementYaw) / 10f).roundToInt().modulus(36)
        return defaultState.with(ROTATION, rotation)
    }

    override fun onBlockActivated(
        state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, handIn: Hand, hit: BlockRayTraceResult
    ): ActionResultType {
        if (!world.isRemote) {
            world.setBlockState(pos, state.with(ROTATION, (state[ROTATION] + 1).rem(36)))
        }
        return ActionResultType.SUCCESS
    }

}