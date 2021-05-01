package dev.weiland.mods.cameracraft.items

import dev.weiland.mods.cameracraft.entity.TripodMinecartEntity
import net.minecraft.block.AbstractRailBlock
import net.minecraft.item.Item
import net.minecraft.item.ItemUseContext
import net.minecraft.state.properties.RailShape
import net.minecraft.tags.BlockTags
import net.minecraft.util.ActionResultType

internal class TripodMinecartItem(properties: Properties) : Item(properties) {

    override fun useOn(context: ItemUseContext): ActionResultType? {
        val level = context.level
        val pos = context.clickedPos
        val blockState = level.getBlockState(pos)

        return if (!blockState.`is`(BlockTags.RAILS)) {
            ActionResultType.FAIL
        } else {
            val itemStack = context.itemInHand
            if (!level.isClientSide) {
                val railShape = if (blockState.block is AbstractRailBlock) (blockState.block as AbstractRailBlock).getRailDirection(blockState, level, pos, null) else RailShape.NORTH_SOUTH
                var yOffset = 0.0
                if (railShape.isAscending) {
                    yOffset = 0.5
                }
                val entity = TripodMinecartEntity(level, pos.x.toDouble() + 0.5, pos.y.toDouble() + 0.0625 + yOffset, pos.z.toDouble() + 0.5)
                if (itemStack.hasCustomHoverName()) {
                    entity.customName = itemStack.hoverName
                }
                level.addFreshEntity(entity)
            }
            itemStack.shrink(1)
            ActionResultType.sidedSuccess(level.isClientSide)
        }
    }

}