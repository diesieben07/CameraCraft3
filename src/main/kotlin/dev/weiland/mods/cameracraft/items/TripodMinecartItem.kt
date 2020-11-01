package dev.weiland.mods.cameracraft.items

import dev.weiland.mods.cameracraft.entity.TripodMinecartEntity
import net.minecraft.block.AbstractRailBlock
import net.minecraft.item.Item
import net.minecraft.item.ItemUseContext
import net.minecraft.state.properties.RailShape
import net.minecraft.tags.BlockTags
import net.minecraft.util.ActionResultType

internal class TripodMinecartItem(properties: Properties) : Item(properties) {

    override fun onItemUse(context: ItemUseContext): ActionResultType? {
        val world = context.world
        val pos = context.pos
        val blockState = world.getBlockState(pos)

        return if (!blockState.isIn(BlockTags.RAILS)) {
            ActionResultType.FAIL
        } else {
            val itemStack = context.item
            if (!world.isRemote) {
                val railShape = if (blockState.block is AbstractRailBlock) (blockState.block as AbstractRailBlock).getRailDirection(blockState, world, pos, null) else RailShape.NORTH_SOUTH
                var yOffset = 0.0
                if (railShape.isAscending) {
                    yOffset = 0.5
                }
                val entity = TripodMinecartEntity(world, pos.x.toDouble() + 0.5, pos.y.toDouble() + 0.0625 + yOffset, pos.z.toDouble() + 0.5)
                if (itemStack.hasDisplayName()) {
                    entity.customName = itemStack.displayName
                }
                world.addEntity(entity)
            }
            itemStack.shrink(1)
            ActionResultType.func_233537_a_(world.isRemote)
        }
    }

}