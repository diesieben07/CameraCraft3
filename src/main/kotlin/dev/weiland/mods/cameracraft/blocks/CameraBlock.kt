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
import net.minecraft.tileentity.TileEntity
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

internal class CameraBlock : Block(Properties.of(Material.METAL).noOcclusion()) {

    companion object {
//        val ROTATION = IntegerProperty.create("rotation", 0, 35)

        private fun makeShape(degrees: Int) {

        }


        val SHAPE = box(1.0, 0.0, 1.0, 15.0, 10.0, 15.0)

    }

    override fun hasTileEntity(state: BlockState?): Boolean {
        return true
    }

    override fun createTileEntity(state: BlockState?, world: IBlockReader?): TileEntity? {
        return CameraTile()
    }

    override fun getShape(state: BlockState, worldIn: IBlockReader, pos: BlockPos, context: ISelectionContext): VoxelShape {
        return SHAPE
    }

    override fun getCollisionShape(state: BlockState, worldIn: IBlockReader, pos: BlockPos, context: ISelectionContext): VoxelShape {
        return SHAPE
    }

    override fun createBlockStateDefinition(builder: StateContainer.Builder<Block, BlockState>) {
//        builder.add(ROTATION)
    }

    override fun updateShape(
        state: BlockState, facing: Direction, facingState: BlockState, world: IWorld, currentPos: BlockPos, facingPos: BlockPos
    ): BlockState {
        return if (!canSurvive(state, world, currentPos)) {
            Blocks.AIR.defaultBlockState()
        } else {
            super.updateShape(state, facing, facingState, world, currentPos, facingPos)
        }
    }

    override fun canSurvive(state: BlockState, world: IWorldReader, pos: BlockPos): Boolean {
        return world.getBlockState(pos.below()).block == CCBlocks.TRIPOD
    }

    override fun getStateForPlacement(context: BlockItemUseContext): BlockState {
        val rotation = ((360 - context.rotation) / 10f).roundToInt().modulus(36)
//        return defaultState.with(ROTATION, rotation)
        return defaultBlockState()
    }

    override fun use(
        state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, handIn: Hand, hit: BlockRayTraceResult
    ): ActionResultType {
        if (!world.isClientSide) {
            (world.getBlockEntity(pos) as? CameraTile)?.let { it.rotation = (it.rotation + 1).rem(CameraTile.ROTATION_STEPS) }
        }
        return ActionResultType.SUCCESS
    }

}